package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.adapter.s3.S3UploaderApi;
import com.liberty52.product.global.event.Events;
import com.liberty52.product.global.event.events.CardOrderedCompletedEvent;
import com.liberty52.product.global.event.events.OrderRequestDepositEvent;
import com.liberty52.product.global.exception.external.badrequest.RequestForgeryPayException;
import com.liberty52.product.global.exception.external.forbidden.NotYourCustomProductException;
import com.liberty52.product.global.exception.external.forbidden.NotYourOrderException;
import com.liberty52.product.global.exception.external.internalservererror.ConfirmPaymentException;
import com.liberty52.product.global.exception.external.notfound.OrderNotFoundByIdException;
import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.service.applicationservice.OrderCreateService;
import com.liberty52.product.service.controller.dto.*;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.entity.payment.Payment;
import com.liberty52.product.service.entity.payment.VBank;
import com.liberty52.product.service.entity.payment.VBankPayment;
import com.liberty52.product.service.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderCreateServiceImpl implements OrderCreateService {

    private static final String RESOURCE_NAME_PRODUCT = "Product";
    private static final String PARAM_NAME_PRODUCT_NAME = "name";
    private static final String RESOURCE_NAME_OPTION_DETAIL = "OptionDetail";
    private static final String PARAM_NAME_OPTION_DETAIL_NAME = "name";
    private final S3UploaderApi s3Uploader;
    private final ProductRepository productRepository;
    private final CustomProductRepository customProductRepository;
    private final OrdersRepository ordersRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final CustomProductOptionRepository customProductOptionRepository;
    private final ConfirmPaymentMapRepository confirmPaymentMapRepository;
    private final VBankRepository vBankRepository;
    private final CartRepository cartRepository;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* API method area */
    @Override
    public PaymentConfirmResponseDto confirmFinalApprovalOfCardPayment(String authId, String orderId) {
        AtomicInteger secTimeout = new AtomicInteger(0);
        while (!confirmPaymentMapRepository.containsOrderId(orderId)) {
            this.sleepingConfirmPaymentThread(orderId, secTimeout);
        }

        Orders checkOrder = confirmPaymentMapRepository.getAndRemove(orderId);
        if (!Objects.equals(authId, checkOrder.getAuthId())) {
            confirmPaymentMapRepository.put(checkOrder.getId(), checkOrder);
            throw new NotYourOrderException(authId);
        }

        Orders orders = ordersRepository.findById(checkOrder.getId())
                .orElseThrow(() -> new OrderNotFoundByIdException(checkOrder.getId()));

        return switch (orders.getPayment().getStatus()) {
            case PAID -> {
                Events.raise(new CardOrderedCompletedEvent(orders));
                yield PaymentConfirmResponseDto.of(orderId, orders.getOrderNum());
            }
            case FORGERY -> throw new RequestForgeryPayException();
            default -> {
                log.error("주문 결제 상태의 PAID or FORGERY 이외의 상태로 요청되었습니다. 요청주문의 상태: {}", orders.getPayment().getStatus());
                throw new ConfirmPaymentException();
            }
        };
    }

    @Override
    public PaymentCardResponseDto createCardPaymentOrders(String authId, OrderCreateRequestDto dto, MultipartFile imageFile) {
        Orders order = this.saveOrder(authId, dto, imageFile);
        this.saveCardPayment(order);
        return PaymentCardResponseDto.of(order.getId(), order.getOrderNum(), order.getAmount());
    }

    @Override
    public PaymentVBankResponseDto createVBankPaymentOrders(String authId, OrderCreateRequestDto dto, MultipartFile imageFile) {
        Orders order = this.saveOrder(authId, dto, imageFile);
        this.saveVBankPayment(dto, order);
        return PaymentVBankResponseDto.of(order.getId(), order.getOrderNum());
    }

    @Override
    public PaymentCardResponseDto createCardPaymentOrdersByCarts(String authId, OrderCreateRequestDto dto) {
        List<CustomProduct> customProducts = this.getCustomProducts(authId, dto);
        Orders order = this.saveOrderByCarts(authId, dto, customProducts);
        this.saveCardPayment(order);
        return PaymentCardResponseDto.of(order.getId(), order.getOrderNum(), order.getAmount());
    }

    @Override
    public PaymentVBankResponseDto createVBankPaymentOrdersByCarts(String authId, OrderCreateRequestDto dto) {
        List<CustomProduct> customProducts = this.getCustomProducts(authId, dto);
        Orders order = this.saveOrderByCarts(authId, dto, customProducts);
        this.saveVBankPayment(dto, order);
        return PaymentVBankResponseDto.of(order.getId(), order.getOrderNum());
    }

    @Override
    public PaymentCardResponseDto createCardPaymentOrdersByCartsForGuest(String authId, OrderCreateRequestDto dto) {
        List<CustomProduct> customProducts = this.getCustomProducts(authId, dto);
        Orders order = this.saveOrderByCarts(dto.getDestinationDto().getReceiverPhoneNumber(), dto, customProducts);
        this.saveCardPayment(order);
        return PaymentCardResponseDto.of(order.getId(), order.getOrderNum(), order.getAmount());
    }

    @Override
    public PaymentVBankResponseDto createVBankPaymentOrdersByCartsForGuest(String authId, OrderCreateRequestDto dto) {
        List<CustomProduct> customProducts = this.getCustomProducts(authId, dto);
        Orders order = this.saveOrderByCarts(dto.getDestinationDto().getReceiverPhoneNumber(), dto, customProducts);
        this.saveVBankPayment(dto, order);
        return PaymentVBankResponseDto.of(order.getId(), order.getOrderNum());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* private method area */
    private Orders saveOrder(String authId, OrderCreateRequestDto dto, MultipartFile imageFile) {
        Product product = this.getProduct(dto);
        List<OptionDetail> optionDetails = this.getOptionDetails(dto);

        OrderDestination orderDestination = this.createOrderDestination(dto);
        Orders order = ordersRepository.save(Orders.create(authId, orderDestination));

        String imgUrl = s3Uploader.upload(imageFile);
        CustomProduct customProduct = this.createCustomProduct(authId, dto, product, order, imgUrl);
        for (OptionDetail detail : optionDetails) {
            this.createCustomProductOptions(customProduct, detail);
        }

        order.calculateTotalValueAndSet();

        return order;
    }

    private Orders saveOrderByCarts(String authId, OrderCreateRequestDto dto, List<CustomProduct> customProducts) {
        OrderDestination orderDestination = this.createOrderDestination(dto);
        Orders order = ordersRepository.save(Orders.create(authId, orderDestination)); // OrderDestination will be saved by cascading

        customProducts.forEach(customProduct -> {
            customProduct.associateWithOrder(order);
            customProduct.getOptions().stream()
                    .peek(CustomProductOption::fixOption)
                    .close();
        });

        order.calculateTotalValueAndSet();

        return order;
    }

    private Product getProduct(OrderCreateRequestDto dto) {
        return productRepository.findByName(dto.getProductDto().getProductName())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME_PRODUCT, PARAM_NAME_PRODUCT_NAME, dto.getProductDto().getProductName()));
    }

    private List<OptionDetail> getOptionDetails(OrderCreateRequestDto dto) {
        return dto.getProductDto().getOptions().stream()
                .map(optionName -> optionDetailRepository.findByName(optionName)
                        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME_OPTION_DETAIL, PARAM_NAME_OPTION_DETAIL_NAME, optionName)))
                .toList();
    }

    private List<CustomProduct> getCustomProducts(String authId, OrderCreateRequestDto dto) {
        return dto.getCustomProductIdList().stream()
                .map(customProductId -> customProductRepository.findById(customProductId)
                        .orElseThrow(() -> new ResourceNotFoundException("CUSTOM_PRODUCT", "ID", customProductId)))
                .peek(customProduct -> {
                    if (!Objects.equals(authId, customProduct.getAuthId())) {
                        throw new NotYourCustomProductException(authId);
                    }
                })
                .toList();
    }

    private CustomProduct createCustomProduct(String authId, OrderCreateRequestDto dto, Product product, Orders order, String imgUrl) {
        CustomProduct customProduct = CustomProduct.create(imgUrl, dto.getProductDto().getQuantity(), authId);
        customProduct.associateWithProduct(product);
        customProduct.associateWithOrder(order);
        customProductRepository.save(customProduct);
        return customProduct;
    }

    private void createCustomProductOptions(CustomProduct customProduct, OptionDetail detail) {
        CustomProductOption customProductOption = CustomProductOption.create();
        customProductOption.associate(customProduct);
        customProductOption.associate(detail);
        customProductOption.fixOption();
        customProductOptionRepository.save(customProductOption);
    }

    private void saveCardPayment(Orders order) {
        Payment<?> payment = Payment.cardOf();
        payment.associate(order);
    }

    private void saveVBankPayment(OrderCreateRequestDto dto, Orders order) {
        order.changeOrderStatusToWaitingDeposit();

        VBank vBank = VBank.ofFullInfo(dto.getVbankDto().getVbankInfo());
        if (!vBankRepository.existsByBankAndAccountAndHolder(vBank.getBank(), vBank.getAccount(), vBank.getHolder())) {
            throw new ResourceNotFoundException("VBANK", "ACCOUNT", dto.getVbankDto().getVbankInfo());
        }

        Payment<?> payment = Payment.vbankOf();
        payment.associate(order);
        payment.setInfo(VBankPayment.VBankPaymentInfo.ofWaitingDeposit(dto.getVbankDto()));

        Events.raise(new OrderRequestDepositEvent(order));
    }

    private OrderDestination createOrderDestination(OrderCreateRequestDto dto) {
        return OrderDestination.create(
                dto.getDestinationDto().getReceiverName(),
                dto.getDestinationDto().getReceiverEmail(),
                dto.getDestinationDto().getReceiverPhoneNumber(),
                dto.getDestinationDto().getAddress1(),
                dto.getDestinationDto().getAddress2(),
                dto.getDestinationDto().getZipCode()
        );
    }

    private void sleepingConfirmPaymentThread(String orderId, AtomicInteger secTimeout) {
        try {
            log.info("DELAY WEBHOOK - OrderID: {}, Delay Time: {}", orderId, secTimeout.get());
            Thread.sleep(1000);
            if(secTimeout.incrementAndGet() > 60) {
                log.error("카드 결제 정보를 확인하는 시간이 초과했습니다. 웹훅 서버를 확인해주세요. OrderId: {}", orderId);
                throw new ConfirmPaymentException();
            }
        } catch (InterruptedException e) {
            log.error("카드결제 검증요청 스레드에 문제가 발생하였습니다. OrderId: {}", orderId);
            throw new ConfirmPaymentException();
        }
    }

    @Override
    @Deprecated
    public MonoItemOrderResponseDto save(String authId, MultipartFile imageFile, MonoItemOrderRequestDto dto) {
        // Valid and get resources
        Product product = productRepository.findByName(dto.getProductName())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME_PRODUCT, PARAM_NAME_PRODUCT_NAME, dto.getProductName()));
        List<OptionDetail> details = new ArrayList<>();
        for (String optionName : dto.getOptions()) {
            OptionDetail optionDetail = optionDetailRepository.findByName(optionName)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME_OPTION_DETAIL, PARAM_NAME_OPTION_DETAIL_NAME, optionName));
            details.add(optionDetail);
        }

        OrderDestination orderDestination = OrderDestination.create("", "", "", "", "", "");

        // Save Order
        Orders order = ordersRepository.save(Orders.create(authId, dto.getDeliveryPrice(), orderDestination)); // OrderDestination will be saved by cascading

        // Upload Image
        String imgUrl = s3Uploader.upload(imageFile);

        // Save CustomProduct
        CustomProduct customProduct = CustomProduct.create(imgUrl, dto.getQuantity(), authId);
        customProduct.associateWithProduct(product);
        customProduct.associateWithOrder(order);
        customProduct = customProductRepository.save(customProduct);

        // Save CustomProductOption
        for (OptionDetail detail : details) {
            CustomProductOption customProductOption = customProductOptionRepository.save(CustomProductOption.create());
            customProductOption.associate(customProduct);
            customProductOption.associate(detail);
        }

        return MonoItemOrderResponseDto.create(order.getId(), order.getOrderDate(), order.getOrderStatus());
    }

}
