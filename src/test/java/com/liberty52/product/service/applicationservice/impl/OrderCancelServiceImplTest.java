package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.MockS3Test;
import com.liberty52.product.TestBeanConfig;
import com.liberty52.product.global.adapter.cloud.AuthServiceClient;
import com.liberty52.product.global.exception.external.badrequest.AlreadyCancelOrderException;
import com.liberty52.product.global.exception.external.badrequest.OrderRefundException;
import com.liberty52.product.global.exception.external.forbidden.InvalidRoleException;
import com.liberty52.product.service.applicationservice.OrderCancelService;
import com.liberty52.product.service.applicationservice.OrderCreateService;
import com.liberty52.product.service.controller.dto.*;
import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.payment.VBankPayment;
import com.liberty52.product.service.repository.OrdersRepository;
import com.liberty52.product.service.utils.TestDtoBuilder;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Import({TestBeanConfig.class})
class OrderCancelServiceImplTest extends MockS3Test {

    @Autowired
    private OrderCancelService orderCancelService;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderCreateService orderCreateService;
    @Autowired
    private AuthServiceClient authServiceClient;

    private final String AUTH_ID = UUID.randomUUID().toString();

    MockMultipartFile imageFile = new MockMultipartFile("image", "test.png", "image/jpeg", new FileInputStream("src/test/resources/static/test.jpg"));

    private static final String LIBERTY = "Liberty 52_Frame";
    private static final String OPTION_1 = "이젤 거치형";
    private static final String OPTION_2 = "1mm 두께 승화전사 인쇄용 알루미늄시트";
    private static final String OPTION_3 = "유광실버";
    private static final int QUANTITY = 2;

    OrderCancelServiceImplTest() throws IOException {
    }

//    @Test
    void test_cancelOrder_vbank_waiting_deposit() {
        final String aid = AUTH_ID;
        OrderCreateRequestDto requestDto = OrderCreateRequestDto.forTestVBank(
                LIBERTY, List.of(OPTION_1, OPTION_2, OPTION_3), QUANTITY, List.of(),
                "테스터", "hsh47607@naver.com", "receiverPhoneNumber", "address1", "address2", "zipCode",
                "하나은행 1234123412341234 리버티", "tester"
        );
        PaymentVBankResponseDto creationDto = orderCreateService.createVBankPaymentOrders(aid, requestDto, imageFile);

        String orderId = creationDto.getOrderId();

        OrderCancelDto.Request request = TestDtoBuilder.orderCancelRequestDto(
                orderId, "취소사유", "국민은행", "김테스터", "1304124-31232-12", "01012341234"
        );
        orderCancelService.cancelOrder(aid, request);

        Orders order = ordersRepository.findById(orderId).get();
        Assertions.assertNotNull(order);
        Assertions.assertNotEquals(OrderStatus.WAITING_DEPOSIT, order.getOrderStatus());
        Assertions.assertEquals(OrderStatus.CANCELED, order.getOrderStatus());

        Assertions.assertNotNull(order.getCanceledOrders());
        Assertions.assertFalse(order.getCanceledOrders().getReason().isBlank());
        Assertions.assertNotNull(order.getCanceledOrders().getReqAt());
        Assertions.assertNotNull(order.getCanceledOrders().getCanceledAt());
        Assertions.assertEquals(0, order.getCanceledOrders().getFee());
    }

//    @Test
    void test_cancelOrder_vbank_ordered() {
        final String aid = AUTH_ID;
        OrderCreateRequestDto requestDto = OrderCreateRequestDto.forTestVBank(
                LIBERTY, List.of(OPTION_1, OPTION_2, OPTION_3), QUANTITY, List.of(),
                "테스터", "hsh47607@naver.com", "receiverPhoneNumber", "address1", "address2", "zipCode",
                "하나은행 1234123412341234 리버티", "tester"
        );
        PaymentVBankResponseDto creationDto = orderCreateService.createVBankPaymentOrders(aid, requestDto, imageFile);

        String orderId = creationDto.getOrderId();
        Orders bOrder = ordersRepository.findById(orderId).get();
        bOrder.changeOrderStatusToOrdered();
        ordersRepository.save(bOrder);

        OrderCancelDto.Request request = TestDtoBuilder.orderCancelRequestDto(
                orderId, "취소사유", "국민은행", "김테스터", "1304124-31232-12", "01012341234"
        );
        orderCancelService.cancelOrder(aid, request);

        Orders order = ordersRepository.findById(orderId).get();
        Assertions.assertNotNull(order);
        Assertions.assertNotEquals(OrderStatus.WAITING_DEPOSIT, order.getOrderStatus());
        Assertions.assertEquals(OrderStatus.CANCEL_REQUESTED, order.getOrderStatus());

        Assertions.assertNotNull(order.getCanceledOrders());
        Assertions.assertFalse(order.getCanceledOrders().getReason().isBlank());
        Assertions.assertNotNull(order.getCanceledOrders().getReqAt());

        VBankPayment.VBankPaymentInfo paymentInfo = order.getPayment().getInfoAsDto();
        Assertions.assertFalse(paymentInfo.getRefundBank().isBlank());
        Assertions.assertFalse(paymentInfo.getRefundHolder().isBlank());
        Assertions.assertFalse(paymentInfo.getRefundAccount().isBlank());
        Assertions.assertFalse(paymentInfo.getRefundPhoneNum().isBlank());
    }

//    @Test
    void test_refundCustomerOrder() {
        final String aid = AUTH_ID;
        OrderCreateRequestDto requestDto = OrderCreateRequestDto.forTestVBank(
                LIBERTY, List.of(OPTION_1, OPTION_2, OPTION_3), QUANTITY, List.of(),
                "테스터", "hsh47607@naver.com", "receiverPhoneNumber", "address1", "address2", "zipCode",
                "하나은행 1234123412341234 리버티", "tester"
        );
        PaymentVBankResponseDto creationDto = orderCreateService.createVBankPaymentOrders(aid, requestDto, imageFile);

        String orderId = creationDto.getOrderId();
        Orders bOrder = ordersRepository.findById(orderId).get();
        bOrder.changeOrderStatusToOrdered();
        ordersRepository.save(bOrder);

        OrderCancelDto.Request cancelRequest = TestDtoBuilder.orderCancelRequestDto(
                orderId, "취소사유", "국민은행", "김테스터", "1304124-31232-12", "01012341234"
        );
        orderCancelService.cancelOrder(aid, cancelRequest);
        Orders cOrder = ordersRepository.findById(orderId).get();

        Assertions.assertEquals(OrderStatus.CANCEL_REQUESTED, cOrder.getOrderStatus());

        OrderRefundDto.Request refundRequest = TestDtoBuilder.orderRefundRequestDto(orderId, 300);
        orderCancelService.refundCustomerOrderByAdmin("ADMIN_ID", "ADMIN", refundRequest);

        Orders order = ordersRepository.findById(orderId).get();
        Assertions.assertNotNull(order);
        Assertions.assertEquals(OrderStatus.CANCELED, order.getOrderStatus());
        Assertions.assertEquals("빌드 테스터", order.getCanceledOrders().getApprovedAdminName());
        Assertions.assertEquals(300, order.getCanceledOrders().getFee());
    }

//    @Test
    void test_refundCustomerOrder_when_givenInvalidPayType_throw_OrderRefundException() {
        final String aid = AUTH_ID;
        PaymentCardResponseDto dto = orderCreateService.createCardPaymentOrders(aid,
                OrderCreateRequestDto.forTestCard(
                        LIBERTY, List.of(OPTION_1, OPTION_2, OPTION_3), 2, List.of(),
                        "receiverName", "receiverEmail", "receiverPhoneNumber", "address1", "address2", "zipCode"),
                imageFile);

        String orderId = dto.getMerchantId();

        OrderRefundDto.Request refundRequest = TestDtoBuilder.orderRefundRequestDto(orderId, 300);
        Assertions.assertThrows(
                OrderRefundException.class,
                () -> orderCancelService.refundCustomerOrderByAdmin("ADMIN_ID", "ADMIN", refundRequest)
        );
    }

//    @Test
    void test_refundCustomerOrder_when_orderIsAlreadyCancel_throw_AlreadyCancelOrderException() {
        final String aid = AUTH_ID;

        OrderCreateRequestDto requestDto = OrderCreateRequestDto.forTestVBank(
                LIBERTY, List.of(OPTION_1, OPTION_2, OPTION_3), QUANTITY, List.of(),
                "테스터", "hsh47607@naver.com", "receiverPhoneNumber", "address1", "address2", "zipCode",
                "하나은행 1234123412341234 리버티", "tester"
        );
        PaymentVBankResponseDto creationDto = orderCreateService.createVBankPaymentOrders(aid, requestDto, imageFile);

        String orderId = creationDto.getOrderId();
        Orders bOrder = ordersRepository.findById(orderId).get();
        bOrder.changeOrderStatusToCanceled();
        ordersRepository.save(bOrder);

        OrderRefundDto.Request refundRequest = TestDtoBuilder.orderRefundRequestDto(orderId, 300);
        Assertions.assertThrows(
                AlreadyCancelOrderException.class,
                () -> orderCancelService.refundCustomerOrderByAdmin("ADMIN_ID", "ADMIN", refundRequest)
        );
    }

//    @Test
    void test_refundCustomerOrder_when_orderIsWaitingDeposit_throw_OrderRefundException() {
        final String aid = AUTH_ID;
        OrderCreateRequestDto requestDto = OrderCreateRequestDto.forTestVBank(
                LIBERTY, List.of(OPTION_1, OPTION_2, OPTION_3), QUANTITY, List.of(),
                "테스터", "hsh47607@naver.com", "receiverPhoneNumber", "address1", "address2", "zipCode",
                "하나은행 1234123412341234 리버티", "tester"
        );
        PaymentVBankResponseDto creationDto = orderCreateService.createVBankPaymentOrders(aid, requestDto, imageFile);
        String orderId = creationDto.getOrderId();

        OrderRefundDto.Request refundRequest = TestDtoBuilder.orderRefundRequestDto(orderId, 300);
        Assertions.assertThrows(
                OrderRefundException.class,
                () -> orderCancelService.refundCustomerOrderByAdmin("ADMIN_ID", "ADMIN", refundRequest)
        );
    }

//    @Test
    void test_refundCustomerOrder_when_requesterIsNotAdmin_throw_InvalidRoleException() {
        final String aid = AUTH_ID;
        OrderCreateRequestDto requestDto = OrderCreateRequestDto.forTestVBank(
                LIBERTY, List.of(OPTION_1, OPTION_2, OPTION_3), QUANTITY, List.of(),
                "테스터", "hsh47607@naver.com", "receiverPhoneNumber", "address1", "address2", "zipCode",
                "하나은행 1234123412341234 리버티", "tester"
        );
        PaymentVBankResponseDto creationDto = orderCreateService.createVBankPaymentOrders(aid, requestDto, imageFile);
        String orderId = creationDto.getOrderId();

        OrderRefundDto.Request refundRequest = TestDtoBuilder.orderRefundRequestDto(orderId, 300);
        Assertions.assertThrows(
                InvalidRoleException.class,
                () -> orderCancelService.refundCustomerOrderByAdmin("ADMIN_ID", "USER", refundRequest)
        );
    }

}