package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.MockAdaptersTest;
import com.liberty52.product.TestBeanConfig;
import com.liberty52.product.service.applicationservice.OrderCancelService;
import com.liberty52.product.service.applicationservice.OrderCreateService;
import com.liberty52.product.service.applicationservice.OrderRetrieveService;
import com.liberty52.product.service.controller.dto.*;
import com.liberty52.product.service.entity.CanceledOrders;
import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.repository.OrdersRepository;
import com.liberty52.product.service.utils.TestDtoBuilder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
@Import({TestBeanConfig.class})
public class CanceledOrderRetrieveTest extends MockAdaptersTest {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderCreateService orderCreateService;
    @Autowired
    private OrderCancelService orderCancelService;
    @Autowired
    private OrderRetrieveService orderRetrieveService;
    @Autowired
    private EntityManager em;

    private final String ADMIN = "ADMIN";
    private final PageRequest pageRequest = PageRequest.of(0, 10);

    public CanceledOrderRetrieveTest() throws IOException {
    }

    @Test
    void test_retrieveCanceledOrdersByAdmin() {
        save_10_canceledOrders();

        AdminCanceledOrderListResponse allResponse = orderRetrieveService.retrieveCanceledOrdersByAdmin(ADMIN, pageRequest);
        Assertions.assertNotNull(allResponse);
        Assertions.assertFalse(allResponse.getOrders().isEmpty());
        Assertions.assertEquals(10, allResponse.getOrders().size());

        AdminCanceledOrderListResponse onlyCanceledResponse = orderRetrieveService.retrieveOnlyCanceledOrdersByAdmin(ADMIN, pageRequest);
        Assertions.assertNotNull(onlyCanceledResponse);
        Assertions.assertFalse(onlyCanceledResponse.getOrders().isEmpty());
        Assertions.assertEquals(6, onlyCanceledResponse.getOrders().size());

        AdminCanceledOrderListResponse onlyRequestedResponse = orderRetrieveService.retrieveOnlyRequestedCanceledOrdersByAdmin(ADMIN, pageRequest);
        Assertions.assertNotNull(onlyRequestedResponse);
        Assertions.assertFalse(onlyRequestedResponse.getOrders().isEmpty());
        Assertions.assertEquals(4, onlyRequestedResponse.getOrders().size());
    }

    @Test
    void test_retrieveCanceledOrderDetailByAdmin() {
        String orderId = save_one_canceledOrder();
        em.flush();
        em.clear();
        AdminCanceledOrderDetailResponse response = orderRetrieveService.retrieveCanceledOrderDetailByAdmin(ADMIN, orderId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(orderId, response.getBasicOrderDetail().getOrderId());
        Assertions.assertEquals(OrderStatus.CANCEL_REQUESTED.name(), response.getBasicOrderDetail().getOrderStatus());
        Assertions.assertEquals(3, response.getBasicOrderDetail().getProducts().get(0).getOptions().size());
        Assertions.assertNotNull(response.getCanceledInfo());
        Assertions.assertEquals("취소사유", response.getCanceledInfo().getReason());
        Assertions.assertEquals(0, response.getCanceledInfo().getFee());

        ordersRepository.deleteById(orderId);
    }

    MockMultipartFile imageFile = new MockMultipartFile("image", "test.png", "image/jpeg", new FileInputStream("src/test/resources/static/test.jpg"));
    private static final String LIBERTY = "Liberty 52_Frame";
    private static final String OPTION_1 = "이젤 거치형";
    private static final String OPTION_2 = "1mm 두께 승화전사 인쇄용 알루미늄시트";
    private static final String OPTION_3 = "유광실버";
    private static final int QUANTITY = 2;
    void save_10_canceledOrders() {
        final String aid = UUID.randomUUID().toString();

        for (int i = 0; i < 6; i++) {
            OrderCreateRequestDto requestDto = OrderCreateRequestDto.forTestVBank(
                    LIBERTY, List.of(OPTION_1, OPTION_2, OPTION_3), QUANTITY, List.of(),
                    "name", "hsh47607@naver.com", "phone", "add1", "add2", "zip",
                    "하나은행 1234123412341234 리버티", "tester"
            );
            PaymentVBankResponseDto responseDto = orderCreateService.createVBankPaymentOrders(aid, requestDto, imageFile);
            String orderId = responseDto.getOrderId();

            OrderCancelDto.Request cancelRequest = TestDtoBuilder.orderCancelRequestDto(
                    orderId, "취소사유", "국민은행", "김테스터", "1304124-31232-12", "01012341234"
            );
            Orders order = ordersRepository.findById(orderId).get();
            CanceledOrders canceledOrders = CanceledOrders.of(cancelRequest.getReason(), order);
            canceledOrders.approveCanceled(0, "SYSTEM");
            order.changeOrderStatusToCanceled();
            ordersRepository.save(order);
        }

        for (int i = 0; i < 4; i++) {
            OrderCreateRequestDto requestDto = OrderCreateRequestDto.forTestCard(
                    LIBERTY, List.of(OPTION_1, OPTION_2, OPTION_3), QUANTITY, List.of(),
                    "name", "hsh47607@naver.com", "phone", "add1", "add2", "zip"
            );
            PaymentCardResponseDto responseDto = orderCreateService.createCardPaymentOrders(aid, requestDto, imageFile);
            String orderId = responseDto.getMerchantId();

            OrderCancelDto.Request cancelRequest = TestDtoBuilder.orderCancelRequestDto(orderId);
            Orders order = ordersRepository.findById(orderId).get();
            CanceledOrders.of(cancelRequest.getReason(), order);
            order.changeOrderStatusToCancelRequest();
            ordersRepository.save(order);
        }
    }

    private String save_one_canceledOrder() {
        final String aid = UUID.randomUUID().toString();

        OrderCreateRequestDto requestDto = OrderCreateRequestDto.forTestVBank(
                LIBERTY, List.of(OPTION_1, OPTION_2, OPTION_3), QUANTITY, List.of(),
                "name", "hsh47607@naver.com", "phone", "add1", "add2", "zip",
                "하나은행 1234123412341234 리버티", "tester"
        );
        PaymentVBankResponseDto responseDto = orderCreateService.createVBankPaymentOrders(aid, requestDto, imageFile);

        String orderId = responseDto.getOrderId();
        Orders order = ordersRepository.findById(orderId).get();
        order.changeOrderStatusToOrdered();
        ordersRepository.save(order);

        OrderCancelDto.Request cancelRequest = TestDtoBuilder.orderCancelRequestDto(
                orderId, "취소사유", "국민은행", "김테스터", "1304124-31232-12", "01012341234"
        );
        orderCancelService.cancelOrder(aid, cancelRequest);

        return orderId;
    }
}
