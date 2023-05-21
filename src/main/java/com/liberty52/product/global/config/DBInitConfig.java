package com.liberty52.product.global.config;

import com.liberty52.product.global.adapter.portone.dto.PortOnePaymentInfo;
import com.liberty52.product.global.contants.PriceConstants;
import com.liberty52.product.global.contants.ProductConstants;
import com.liberty52.product.global.contants.VBankConstants;
import com.liberty52.product.service.applicationservice.OrderCreateService;
import com.liberty52.product.service.controller.dto.OrderCancelDto;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.entity.payment.CardPayment;
import com.liberty52.product.service.entity.payment.Payment;
import com.liberty52.product.service.entity.payment.Payment.PaymentInfo;
import com.liberty52.product.service.entity.payment.VBank;
import com.liberty52.product.service.entity.payment.VBankPayment;
import com.liberty52.product.service.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DBInitConfig {

    private final DBInitService initService;

    @PostConstruct
    public void init() {
        initService.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    @Slf4j
    public static class DBInitService {

        private final OrderCreateService orderCreateService;
        private final CartItemRepository customProductRepository;
        private final ProductRepository productRepository;
        private final ProductOptionRepository productOptionRepository;
        private final OptionDetailRepository optionDetailRepository;
        private final CartRepository cartRepository;
        private final CustomProductOptionRepository customProductOptionRepository;
        private final OrdersRepository ordersRepository;
        public static final String AUTH_ID = "TESTER-001";
        public static final String LIBERTY = "Liberty 52_Frame";
        private static Orders order;
        private static Product product;
        private static Review review;

        private static CustomProduct customProduct;
        private final ReviewRepository reviewRepository;
        private final VBankRepository vBankRepository;

        private final Environment env;

        public void init() {
            try {
                Product product = Product.create(LIBERTY, ProductState.ON_SAIL, 100L);
                Field id = product.getClass().getDeclaredField("id");
                id.setAccessible(true);
                id.set(product, "LIB-001");

                productRepository.save(product);
                DBInitService.product = product;

                ProductOption option1 = ProductOption.create(ProductConstants.PROD_OPT_1, true, true);
                option1.associate(product);
                productOptionRepository.save(option1);

                OptionDetail detailEasel = OptionDetail.create("이젤 거치형", 100,true);
                detailEasel.associate(option1);
                Field detailEaselId = detailEasel.getClass().getDeclaredField("id");
                detailEaselId.setAccessible(true);
                detailEaselId.set(detailEasel, "OPT-001");
                optionDetailRepository.save(detailEasel);

                OptionDetail detailWall = OptionDetail.create("벽걸이형", 100,true);
                detailWall.associate(option1);
                Field detailWallId = detailWall.getClass().getDeclaredField("id");
                detailWallId.setAccessible(true);
                detailWallId.set(detailWall, "OPT-002");
                optionDetailRepository.save(detailWall);

                ProductOption option2 = ProductOption.create(ProductConstants.PROD_OPT_2, true, true);
                option2.associate(product);
                productOptionRepository.save(option2);

                OptionDetail material = OptionDetail.create("1mm 두께 승화전사 인쇄용 알루미늄시트", 100,true);
                material.associate(option2);
                Field materialId = material.getClass().getDeclaredField("id");
                materialId.setAccessible(true);
                materialId.set(material, "OPT-003");
                optionDetailRepository.save(material);

                ProductOption option3 = ProductOption.create(ProductConstants.PROD_OPT_3, true, true);
                option3.associate(product);
                productOptionRepository.save(option3);

                OptionDetail materialOption1 = OptionDetail.create("유광실버", 100,true);
                materialOption1.associate(option3);
                Field materialOption1Id = materialOption1.getClass().getDeclaredField("id");
                materialOption1Id.setAccessible(true);
                materialOption1Id.set(materialOption1, "OPT-004");
                optionDetailRepository.save(materialOption1);

                OptionDetail materialOption2 = OptionDetail.create("무광실버", 100,true);
                materialOption2.associate(option3);
                optionDetailRepository.save(materialOption2);

                OptionDetail materialOption3 = OptionDetail.create("유광백색", 100,true);
                materialOption3.associate(option3);
                optionDetailRepository.save(materialOption3);

                OptionDetail materialOption4 = OptionDetail.create("무광백색", 100,true);
                materialOption4.associate(option3);
                optionDetailRepository.save(materialOption4);

                // Add Cart & CartItems
                Cart cart = cartRepository.save(Cart.create(AUTH_ID));

                final String imageUrl = env.getProperty(
                        "product.representative-url.liberty52-frame");
                CustomProduct customProduct0 = CustomProduct.create(imageUrl, 1, AUTH_ID);
                customProduct0.associateWithProduct(product);
                customProduct0.associateWithCart(cart);
                customProductRepository.save(customProduct0);

                associateCustomProductOption(false, detailEasel, material, materialOption2, customProduct0);

                // Add Order
                Orders order = ordersRepository.save(
                        Orders.create(AUTH_ID, PriceConstants.DEFAULT_DELIVERY_PRICE,
                                OrderDestination.create("receiver", "email", "01012341234",
                                        "경기도 어딘가",
                                        "101동 101호", "12345")));
                DBInitService.order = order;

                customProduct0 = CustomProduct.create(imageUrl, 1, AUTH_ID);
                Field customProductId = customProduct0.getClass().getDeclaredField("id");
                customProductId.setAccessible(true);
                customProductId.set(customProduct0, "Custom_Product_Id");

                customProduct0.associateWithProduct(product);
                customProduct0.associateWithOrder(order);
                customProductRepository.save(customProduct0);

                associateCustomProductOption(true, detailEasel, material, materialOption2, customProduct0);
                Payment<?> payment = Payment.cardOf();
                PortOnePaymentInfo info = PortOnePaymentInfo.testOf(
                        UUID.randomUUID().toString(), UUID.randomUUID().toString(), 100L,
                        UUID.randomUUID().toString());
                payment.associate(order);
                payment.setInfo(CardPayment.CardPaymentInfo.of(info));
                payment.changeStatusToPaid();
                order.calculateTotalValueAndSet();


                // Add Order
                Orders orderSub
                        = ordersRepository.save(
                        Orders.create(AUTH_ID, PriceConstants.DEFAULT_DELIVERY_PRICE,
                                OrderDestination.create("receiver", "email", "01012341234",
                                        "경기도 어딘가",
                                        "101동 101호", "12345")));
                DBInitService.order = order;

                CustomProduct customProduct = CustomProduct.create(imageUrl, 1, AUTH_ID);
                customProduct.associateWithProduct(product);
                customProduct.associateWithOrder(orderSub);
                customProductRepository.save(customProduct);

               // Add Review
                Review review = Review.create(3, "good");
                review.associate(customProduct);
                ReviewImage.create(review, imageUrl);

                for (int i = 0; i < 3; i++) {
                    Reply reply = Reply.create("맛있따" + i, AUTH_ID);
                    reply.associate(review);
                }
                reviewRepository.save(review);

                associateCustomProductOption(true, detailEasel, material, materialOption2, customProduct);
                Payment<? extends PaymentInfo> vbank = Payment.vbankOf();
                vbank.setInfo(VBankPayment.VBankPaymentInfo.of("하나은행 1234123412341234 리버티","하나은행", "김테스터", "138-978554-10547",false));
                vbank.associate(orderSub);
                orderSub.calculateTotalValueAndSet();

                for (int i = 0; i < 10; i++) {
                    Orders guestOrder = Orders.create("GUEST-00"+i,
                            PriceConstants.DEFAULT_DELIVERY_PRICE,
                            OrderDestination.create("receiver", "email", "01012341234", "경기도 어딘가",
                                    "101동 101호", "12345"));

                    Field guestOrderId = guestOrder.getClass().getDeclaredField("id");
                    guestOrderId.setAccessible(true);
                    guestOrderId.set(guestOrder, "GORDER-00"+i);
                    ordersRepository.save(guestOrder);
                    Payment<? extends PaymentInfo> guestPayment = Payment.cardOf();
                    PortOnePaymentInfo cardInfo = PortOnePaymentInfo.testOf(
                            UUID.randomUUID().toString(), UUID.randomUUID().toString(), 100L,
                            UUID.randomUUID().toString());
                    guestPayment.associate(guestOrder);
                    guestPayment.setInfo(CardPayment.CardPaymentInfo.of(cardInfo));
                    guestPayment.changeStatusToPaid();

                    customProduct = CustomProduct.create(imageUrl, 1, "GUEST-00"+i);
                    customProduct.associateWithProduct(product);
                    customProduct.associateWithOrder(guestOrder);
                    customProductRepository.save(customProduct);

                    associateCustomProductOption(true, detailEasel, material, materialOption2, customProduct);

                    Review noPhotoReview = Review.create(3, "good");
                    noPhotoReview.associate(customProduct);

                    reviewRepository.save(noPhotoReview);

                    guestOrder.calculateTotalValueAndSet();
                    ordersRepository.save(guestOrder);
                }

                for (int i = 11; i < 15; i++) {
                    Orders guestOrder = Orders.create("GUEST-00"+i,
                        PriceConstants.DEFAULT_DELIVERY_PRICE,
                        OrderDestination.create("receiver", "email", "01012341234", "경기도 어딘가",
                            "101동 101호", "12345"));
                    guestOrder.changeOrderStatusToWaitingDeposit();
                    Field guestOrderId = guestOrder.getClass().getDeclaredField("id");
                    guestOrderId.setAccessible(true);
                    guestOrderId.set(guestOrder, "GORDER-00"+i);
                    ordersRepository.save(guestOrder);
                    Payment<? extends PaymentInfo> guestPayment = Payment.vbankOf();
                    guestPayment.associate(guestOrder);
                    guestPayment.setInfo(VBankPayment.VBankPaymentInfo.of("하나은행","하나하나은행","박찬길",
                        "123-3123123",false));
                    guestPayment.changeStatusToPaid();

                    customProduct = CustomProduct.create(imageUrl, 1, "GUEST-00"+i);
                    customProduct.associateWithProduct(product);
                    customProduct.associateWithOrder(guestOrder);
                    customProductRepository.save(customProduct);

                    associateCustomProductOption(true, detailEasel, material, materialOption2, customProduct);

                    Review noPhotoReview = Review.create(3, "good");
                    noPhotoReview.associate(customProduct);

                    reviewRepository.save(noPhotoReview);

                    guestOrder.calculateTotalValueAndSet();
                    ordersRepository.save(guestOrder);
                }

                // 주문 취소 데이터 생성
                int c = 0;
                    // 카드 취소
                for (; c < 5; c++) {
                    Orders c_order = Orders.create("CANCELER-00" + c,
                            PriceConstants.DEFAULT_DELIVERY_PRICE,
                            OrderDestination.create("receiver", "email", "01012341234", "경기도 어딘가",
                                    "101동 101호", "12345")
                    );
                    Field orderId = c_order.getClass().getDeclaredField("id");
                    orderId.setAccessible(true);
                    orderId.set(c_order, "CORDER-00"+c);
                    ordersRepository.save(c_order);

                    Payment<? extends PaymentInfo> c_payment = Payment.cardOf();
                    PortOnePaymentInfo cardInfo = PortOnePaymentInfo.testOf(
                            UUID.randomUUID().toString(), UUID.randomUUID().toString(), 100L,
                            UUID.randomUUID().toString());
                    c_payment.associate(c_order);
                    c_payment.setInfo(CardPayment.CardPaymentInfo.of(cardInfo));
                    c_payment.changeStatusToPaid();

                    customProduct = CustomProduct.create(imageUrl, 1, "CANCELER-00"+c);
                    customProduct.associateWithProduct(product);
                    customProduct.associateWithOrder(c_order);
                    customProductRepository.save(customProduct);

                    associateCustomProductOption(true, detailEasel, material, materialOption2, customProduct);

                    c_order.calculateTotalValueAndSet();

                    CanceledOrders canceledOrders = CanceledOrders.of("취소사유-00"+c, c_order);
                    canceledOrders.approveCanceled(0, "SYSTEM");
                    c_order.changeOrderStatusToCanceled();

                    ordersRepository.save(c_order);
                }

                for (; c < 10; c++) {
                    Orders c_order = Orders.create("CANCELER-00" + c,
                            PriceConstants.DEFAULT_DELIVERY_PRICE,
                            OrderDestination.create("receiver", "email", "01012341234", "경기도 어딘가",
                                    "101동 101호", "12345")
                    );
                    Field orderId = c_order.getClass().getDeclaredField("id");
                    orderId.setAccessible(true);
                    orderId.set(c_order, "CORDER-00"+c);
                    ordersRepository.save(c_order);

                    Payment<? extends PaymentInfo> guestPayment = Payment.vbankOf();
                    guestPayment.associate(c_order);
                    guestPayment.setInfo(VBankPayment.VBankPaymentInfo.of(VBankConstants.VBANK_HANA,"국민은행","취소자",
                            "123-3123123",false));

                    customProduct = CustomProduct.create(imageUrl, 1, "CANCELER-00"+c);
                    customProduct.associateWithProduct(product);
                    customProduct.associateWithOrder(c_order);
                    customProductRepository.save(customProduct);

                    associateCustomProductOption(true, detailEasel, material, materialOption2, customProduct);

                    c_order.calculateTotalValueAndSet();

                    CanceledOrders canceledOrders = CanceledOrders.of("취소사유-00"+c, c_order);
                    canceledOrders.approveCanceled(0, "관리자");
                    c_order.changeOrderStatusToCanceled();

                    ordersRepository.save(c_order);
                }

                for (; c < 15; c++) {
                    Orders c_order = Orders.create("CANCELER-00" + c,
                            PriceConstants.DEFAULT_DELIVERY_PRICE,
                            OrderDestination.create("receiver", "email", "01012341234", "경기도 어딘가",
                                    "101동 101호", "12345")
                    );
                    Field orderId = c_order.getClass().getDeclaredField("id");
                    orderId.setAccessible(true);
                    orderId.set(c_order, "CORDER-00"+c);
                    ordersRepository.save(c_order);

                    Payment<? extends PaymentInfo> guestPayment = Payment.vbankOf();
                    guestPayment.associate(c_order);
                    VBankPayment.VBankPaymentInfo prev = VBankPayment.VBankPaymentInfo.of(VBankConstants.VBANK_HANA, "국민은행", "취소자",
                            "123-3123123", false);
                    guestPayment.setInfo(VBankPayment.VBankPaymentInfo.ofRefund(prev, OrderCancelDto.Request.RefundVO.forTest()));
                    guestPayment.changeStatusToPaid();

                    customProduct = CustomProduct.create(imageUrl, 1, "CANCELER-00"+c);
                    customProduct.associateWithProduct(product);
                    customProduct.associateWithOrder(c_order);
                    customProductRepository.save(customProduct);

                    associateCustomProductOption(true, detailEasel, material, materialOption2, customProduct);

                    c_order.calculateTotalValueAndSet();

                    CanceledOrders.of("취소사유-00"+c, c_order);
                    c_order.changeOrderStatusToCancelRequest();

                    ordersRepository.save(c_order);
                }



                VBank vBank_hana = VBank.of(VBankConstants.VBANK_HANA);
                VBank vBank_kb = VBank.of(VBankConstants.VBANK_KB);
                vBankRepository.saveAll(List.of(vBank_hana, vBank_kb));

                // 아래 save가 없어도 DB엔 정상적으로 들어가지만, 테스트에선 반영이 안 됨.
                DBInitService.order = ordersRepository.save(order);
                DBInitService.product = productRepository.save(product);
                DBInitService.review = reviewRepository.save(review);
                DBInitService.customProduct = customProductRepository.save(customProduct0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void associateCustomProductOption(boolean isOrdered, OptionDetail detailEasel, OptionDetail material,
                OptionDetail materialOption2, CustomProduct customProduct) {
            CustomProductOption customProductOption = CustomProductOption.create();
            customProductOption.associate(detailEasel);
            customProductOption.associate(customProduct);
            if (isOrdered) customProductOption.fixOption();
            customProductOptionRepository.save(customProductOption);

            customProductOption = CustomProductOption.create();
            customProductOption.associate(material);
            customProductOption.associate(customProduct);
            if (isOrdered) customProductOption.fixOption();
            customProductOptionRepository.save(customProductOption);

            customProductOption = CustomProductOption.create();
            customProductOption.associate(materialOption2);
            customProductOption.associate(customProduct);
            if (isOrdered) customProductOption.fixOption();
            customProductOptionRepository.save(customProductOption);

        }

        public static Orders getOrder() {
            return order;
        }

        public static Product getProduct() {
            return product;
        }
        public static Review getReview() {
            return review;
        }

        public static CustomProduct getCustomProduct() {return customProduct;}

    }
}
