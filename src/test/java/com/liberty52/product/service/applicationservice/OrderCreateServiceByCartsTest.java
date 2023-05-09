package com.liberty52.product.service.applicationservice;

import com.liberty52.product.MockS3Test;
import com.liberty52.product.service.controller.dto.*;
import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.payment.PaymentStatus;
import com.liberty52.product.service.entity.payment.PaymentType;
import com.liberty52.product.service.entity.payment.VBankPayment;
import com.liberty52.product.service.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderCreateServiceByCartsTest extends MockS3Test {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomProductRepository customProductRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OptionDetailRepository optionDetailRepository;
    @Autowired
    private CustomProductOptionRepository customProductOptionRepository;
    @Autowired
    private ConfirmPaymentMapRepository confirmPaymentMapRepository;
    @Autowired
    private VBankRepository vBankRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemCreateService cartItemCreateService;
    @Autowired
    private CartItemRetriveService cartItemRetriveService;
    @Autowired
    private OrderCreateService orderCreateService;
    @Autowired
    private ProductOptionRepository productOptionRepository;

    final String aid = UUID.randomUUID().toString();
    MockMultipartFile imageFile = new MockMultipartFile("image", "test.png", "image/jpeg", new FileInputStream("src/test/resources/static/test.jpg"));


    public OrderCreateServiceByCartsTest() throws IOException {
    }


    @Test
    void test_preregisterCardPaymentOrdersByCarts() {
        List<String> customProductIdList = cartItemRetriveService.retriveAuthCartItem(aid).stream()
                .map(CartItemResponse::getId).toList();

        PaymentCardResponseDto orderResponseDto = orderCreateService.createCardPaymentOrdersByCarts(
                aid,
                OrderCreateRequestDto.forTestCardByCarts(customProductIdList, "receiverName", "receiverEmail", "receiverPhoneNumber", "address1", "address2", "zipCode")
        );

        String orderId = orderResponseDto.getMerchantId();
        Orders order = ordersRepository.findById(orderId).get();

        Assertions.assertEquals(aid, order.getAuthId());
        Assertions.assertEquals(3, order.getCustomProducts().size());
        Assertions.assertEquals(6, order.getTotalQuantity());
        Assertions.assertEquals("receiverName", order.getOrderDestination().getReceiverName());
        Assertions.assertEquals("receiverEmail", order.getOrderDestination().getReceiverEmail());
        Assertions.assertEquals("receiverPhoneNumber", order.getOrderDestination().getReceiverPhoneNumber());
        Assertions.assertEquals("address1", order.getOrderDestination().getAddress1());
        Assertions.assertEquals("address2", order.getOrderDestination().getAddress2());
        Assertions.assertEquals("zipCode", order.getOrderDestination().getZipCode());

        Assertions.assertEquals(OrderStatus.READY, order.getOrderStatus());
        Assertions.assertNotEquals(0, order.getAmount());

        Assertions.assertEquals(PaymentType.CARD, order.getPayment().getType());
        Assertions.assertEquals(PaymentStatus.READY, order.getPayment().getStatus());
        Assertions.assertSame(order, order.getPayment().getOrders());
        Assertions.assertTrue(order.getPayment().getInfoAsString().isBlank());
    }

    @Test
    void test_registerVBankPaymentOrdersByCarts() {
        List<String> customProductIdList = cartItemRetriveService.retriveAuthCartItem(aid).stream()
                .map(CartItemResponse::getId).toList();

        PaymentVBankResponseDto orderResponseDto = orderCreateService.createVBankPaymentOrdersByCarts(
                aid,
                OrderCreateRequestDto.forTestVBankByCarts(
                        customProductIdList,
                        "receiverName", "receiverEmail", "receiverPhoneNumber", "address1", "address2", "zipCode",
                        "하나은행 1234123412341234 리버티", "tester"
                )
        );

        String orderId = orderResponseDto.getOrderId();
        Orders order = ordersRepository.findById(orderId).get();

        Assertions.assertNotNull(order);
        Assertions.assertEquals(aid, order.getAuthId());
        Assertions.assertEquals(3, order.getCustomProducts().size());
        Assertions.assertEquals(6, order.getTotalQuantity());
        Assertions.assertEquals("receiverName", order.getOrderDestination().getReceiverName());
        Assertions.assertEquals("receiverEmail", order.getOrderDestination().getReceiverEmail());
        Assertions.assertEquals("receiverPhoneNumber", order.getOrderDestination().getReceiverPhoneNumber());
        Assertions.assertEquals("address1", order.getOrderDestination().getAddress1());
        Assertions.assertEquals("address2", order.getOrderDestination().getAddress2());
        Assertions.assertEquals("zipCode", order.getOrderDestination().getZipCode());

        Assertions.assertEquals(OrderStatus.WAITING_DEPOSIT, order.getOrderStatus());
        Assertions.assertNotEquals(0, order.getAmount());

        Assertions.assertEquals(PaymentType.VBANK, order.getPayment().getType());
        Assertions.assertEquals(PaymentStatus.READY, order.getPayment().getStatus());
        Assertions.assertNotEquals("", order.getPayment().getInfoAsString());
        Assertions.assertEquals("하나은행 1234123412341234 리버티", ((VBankPayment.VBankPaymentInfo)(order.getPayment().getInfoAsDto())).getVbankInfo());
        Assertions.assertEquals("tester", ((VBankPayment.VBankPaymentInfo)(order.getPayment().getInfoAsDto())).getDepositorName());
    }

    @Test
    void test_createOrdersByCartForGuest() {
        final String receiverPhoneNum = "01012341234";
        List<String> customProductIdList = cartItemRetriveService.retriveAuthCartItem(aid).stream()
                .map(CartItemResponse::getId).toList();

        PaymentCardResponseDto orderResponseDto = orderCreateService.createCardPaymentOrdersByCartsForGuest(
                aid,
                OrderCreateRequestDto.forTestCardByCarts(customProductIdList, "receiverName", "receiverEmail", receiverPhoneNum, "address1", "address2", "zipCode")
        );
        String orderId = orderResponseDto.getMerchantId();
        Orders order = ordersRepository.findById(orderId).get();

        Assertions.assertEquals(receiverPhoneNum, order.getAuthId());
        Assertions.assertEquals(3, order.getCustomProducts().size());
        Assertions.assertEquals(6, order.getTotalQuantity());
        Assertions.assertEquals("receiverName", order.getOrderDestination().getReceiverName());
        Assertions.assertEquals("receiverEmail", order.getOrderDestination().getReceiverEmail());
        Assertions.assertEquals(receiverPhoneNum, order.getOrderDestination().getReceiverPhoneNumber());
        Assertions.assertEquals("address1", order.getOrderDestination().getAddress1());
        Assertions.assertEquals("address2", order.getOrderDestination().getAddress2());
        Assertions.assertEquals("zipCode", order.getOrderDestination().getZipCode());

        Assertions.assertEquals(OrderStatus.READY, order.getOrderStatus());
        Assertions.assertNotEquals(0, order.getAmount());

        Assertions.assertEquals(PaymentType.CARD, order.getPayment().getType());
        Assertions.assertEquals(PaymentStatus.READY, order.getPayment().getStatus());
        Assertions.assertSame(order, order.getPayment().getOrders());
        Assertions.assertTrue(order.getPayment().getInfoAsString().isBlank());
    }

    @Test
    void test_test_createOrdersByVBankForGuest() {
        final String receiverPhoneNum = "01012341234";
        List<String> customProductIdList = cartItemRetriveService.retriveAuthCartItem(aid).stream()
                .map(CartItemResponse::getId).toList();

        PaymentVBankResponseDto orderResponseDto = orderCreateService.createVBankPaymentOrdersByCartsForGuest(
                aid,
                OrderCreateRequestDto.forTestVBankByCarts(
                        customProductIdList,
                        "receiverName", "receiverEmail", receiverPhoneNum, "address1", "address2", "zipCode",
                        "하나은행 1234123412341234 리버티", "tester"
                )
        );

        String orderId = orderResponseDto.getOrderId();
        Orders order = ordersRepository.findById(orderId).get();

        Assertions.assertNotNull(order);
        Assertions.assertEquals(receiverPhoneNum, order.getAuthId());
        Assertions.assertEquals(3, order.getCustomProducts().size());
        Assertions.assertEquals(6, order.getTotalQuantity());
        Assertions.assertEquals("receiverName", order.getOrderDestination().getReceiverName());
        Assertions.assertEquals("receiverEmail", order.getOrderDestination().getReceiverEmail());
        Assertions.assertEquals(receiverPhoneNum, order.getOrderDestination().getReceiverPhoneNumber());
        Assertions.assertEquals("address1", order.getOrderDestination().getAddress1());
        Assertions.assertEquals("address2", order.getOrderDestination().getAddress2());
        Assertions.assertEquals("zipCode", order.getOrderDestination().getZipCode());

        Assertions.assertEquals(OrderStatus.WAITING_DEPOSIT, order.getOrderStatus());
        Assertions.assertNotEquals(0, order.getAmount());

        Assertions.assertEquals(PaymentType.VBANK, order.getPayment().getType());
        Assertions.assertEquals(PaymentStatus.READY, order.getPayment().getStatus());
        Assertions.assertNotEquals("", order.getPayment().getInfoAsString());
        Assertions.assertEquals("하나은행 1234123412341234 리버티", ((VBankPayment.VBankPaymentInfo)(order.getPayment().getInfoAsDto())).getVbankInfo());
        Assertions.assertEquals("tester", ((VBankPayment.VBankPaymentInfo)(order.getPayment().getInfoAsDto())).getDepositorName());

    }

    @BeforeEach
    void initCarts() {
        CartItemRequest dto1 = new CartItemRequest();
        String[] option1 = {"이젤 거치형", "1mm 두께 승화전사 인쇄용 알루미늄시트", "무광실버"};
        dto1.create("Liberty 52_Frame", 1, option1);
        cartItemCreateService.createAuthCartItem(aid, imageFile, dto1);

        CartItemRequest dto2 = new CartItemRequest();
        String[] option2 = {"이젤 거치형", "1mm 두께 승화전사 인쇄용 알루미늄시트", "무광실버"};
        dto2.create("Liberty 52_Frame", 2, option2);
        cartItemCreateService.createAuthCartItem(aid, imageFile, dto2);

        CartItemRequest dto3 = new CartItemRequest();
        String[] option3 = {"이젤 거치형", "1mm 두께 승화전사 인쇄용 알루미늄시트", "무광실버"};
        dto3.create("Liberty 52_Frame", 3, option3);
        cartItemCreateService.createAuthCartItem(aid, imageFile, dto3);
    }

}
