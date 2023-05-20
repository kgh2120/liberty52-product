package com.liberty52.product.service.applicationservice;

//import static com.liberty52.product.global.contants.RepresentImageUrl.LIBERTY52_FRAME_REPRESENTATIVE_URL;
//import static com.liberty52.product.service.utils.MockConstants.MOCK_ADDRESS;
//import static com.liberty52.product.service.utils.MockConstants.MOCK_AUTH_ID;
//import static com.liberty52.product.service.utils.MockConstants.MOCK_ORDER_STATUS_ORDERED;
//import static com.liberty52.product.service.utils.MockConstants.MOCK_PRICE;
//import static com.liberty52.product.service.utils.MockConstants.MOCK_PRODUCT_NAME;
//import static com.liberty52.product.service.utils.MockConstants.MOCK_PRODUCT_REPRESENT_URL;
//import static com.liberty52.product.service.utils.MockConstants.MOCK_QUANTITY;
//import static com.liberty52.product.service.utils.MockConstants.MOCK_RECEIVER_EMAIL;
//import static com.liberty52.product.service.utils.MockConstants.MOCK_RECEIVER_NAME;
//import static com.liberty52.product.service.utils.MockConstants.MOCK_RECEIVER_PHONE_NUMBER;

import com.liberty52.product.TestBeanConfig;
import com.liberty52.product.global.adapter.cloud.AuthServiceClient;
import com.liberty52.product.global.exception.external.badrequest.CannotAccessOrderException;
import com.liberty52.product.global.exception.external.forbidden.InvalidRoleException;
import com.liberty52.product.service.controller.dto.AdminOrderListResponse;
import com.liberty52.product.service.controller.dto.OrderDetailRetrieveResponse;
import com.liberty52.product.service.utils.MockConstants;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static com.liberty52.product.service.utils.TestInitiator.initDataForTestingOrder;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
@Import({TestBeanConfig.class})
class OrderRetrieveServiceTest {

    @Autowired
    OrderRetrieveService orderRetrieveService;
    @Autowired
    AuthServiceClient authServiceClient;

    @Autowired
    EntityManager em;

    String orderId;
    @BeforeEach
    void beforeEach(){
        orderId = initDataForTestingOrder(em);
    }

    @AfterEach
    void afterEach(){
        em.clear();
    }


    @Test
    void retrieveOrdersTest () throws Exception{
//        //given
//        //when
//        List<OrdersRetrieveResponse> responses = orderRetrieveService.retrieveOrders(
//                MOCK_AUTH_ID);
//        //then
//        assertThat(responses.size()).isSameAs(1);
//        assertThat(responses.get(0).getOrderDate()).isEqualTo(LocalDate.now().toString());
//        assertThat(responses.get(0).getReceiverEmail()).isEqualTo(MOCK_RECEIVER_EMAIL);
//        assertThat(responses.get(0).getReceiverPhoneNumber()).isEqualTo(MOCK_RECEIVER_PHONE_NUMBER);
//        assertThat(responses.get(0).getReceiverName()).isEqualTo(MOCK_RECEIVER_NAME);
    }

    @Test
    void retrieveOrderDetailTest () throws Exception{
//        //given
//        OrderDetailRetrieveResponse response = orderRetrieveService.retrieveOrderDetail(
//                MOCK_AUTH_ID, orderId);
//        //when
//        assertThat(response.getOrderId()).isEqualTo(orderId);
//        assertThat(response.getOrderDate()).isEqualTo(LocalDate.now().toString());
//        assertThat(response.getDeliveryFee()).isEqualTo(0);
//        assertThat(response.getOrderStatus()).isEqualTo(MOCK_ORDER_STATUS_ORDERED.name());
//        assertThat(response.getTotalPrice()).isEqualTo(MOCK_PRICE + 1300000);
//        assertThat(response.getTotalProductPrice()).isEqualTo(MOCK_PRICE + 1300000);
//        assertThat(response.getReceiverName()).isEqualTo(MOCK_RECEIVER_NAME);
//        assertThat(response.getReceiverEmail()).isEqualTo(MOCK_RECEIVER_EMAIL);
//        assertThat(response.getReceiverPhoneNumber()).isEqualTo(MOCK_RECEIVER_PHONE_NUMBER);
//        assertThat(response.getProductRepresentUrl()).isEqualTo(LIBERTY52_FRAME_REPRESENTATIVE_URL);
//        assertThat(response.getAddress()).isEqualTo(MOCK_ADDRESS+" "+MOCK_ADDRESS);
//        assertThat(response.getProducts().get(0).getName()).isEqualTo(MOCK_PRODUCT_NAME);
//        assertThat(response.getProducts().get(0).getPrice()).isEqualTo(MOCK_PRICE + 1300000);
//        assertThat(response.getProducts().get(0).getQuantity()).isEqualTo(MOCK_QUANTITY);
//        assertThat(response.getProducts().get(0).getProductUrl()).isEqualTo(MOCK_PRODUCT_REPRESENT_URL);
    }
    @Test
    void retrieveOrderDetail_Throw_cannot_access_order () throws Exception{
        //given
        //when
        assertThatThrownBy(() ->  orderRetrieveService.retrieveGuestOrderDetail(
                "WrongID", "WrongID"))
                .isInstanceOf(CannotAccessOrderException.class);

        //then


    }

    @Test
    void test_retrieveOrdersByAdmin() {
        final String ROLE_ADMIN = "ADMIN";
        final int page = 0;
        final int size = 10;

        AdminOrderListResponse response = orderRetrieveService.retrieveOrdersByAdmin(ROLE_ADMIN, PageRequest.of(page, size));

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getOrders());
        Assertions.assertFalse(response.getOrders().isEmpty());
        Assertions.assertEquals(size, response.getOrders().size());
        response.getOrders().forEach(res -> {
            Assertions.assertEquals(MockConstants.MOCK_AUTHOR_NAME, res.getCustomerName());
        });
        Assertions.assertEquals(1, response.getStartPage());
        Assertions.assertEquals(page+1, response.getCurrentPage());
    }

    @Test
    void test_retrieveOrdersByAdmin_throw_InvalidRoleException() {
        final String ROLE_INVALID = "USER";

        Assertions.assertThrows(
                InvalidRoleException.class,
                () -> orderRetrieveService.retrieveOrdersByAdmin(ROLE_INVALID, PageRequest.of(0, 10))
        );
    }

    @Test
    void test_retrieveOrderDetailByAdmin() {
        final String ROLE_ADMIN = "ADMIN";
        final String ORDER_ID = "GORDER-000";

        OrderDetailRetrieveResponse response = orderRetrieveService.retrieveOrderDetailByAdmin(ROLE_ADMIN, ORDER_ID);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(ORDER_ID, response.getOrderId());
        Assertions.assertFalse(response.getOrderDate().isBlank());
        Assertions.assertFalse(response.getOrderStatus().isBlank());
        Assertions.assertFalse(response.getAddress().isBlank());
        Assertions.assertFalse(response.getReceiverEmail().isBlank());
        Assertions.assertFalse(response.getReceiverName().isBlank());
        Assertions.assertFalse(response.getReceiverEmail().isBlank());
        Assertions.assertFalse(response.getReceiverPhoneNumber().isBlank());
        Assertions.assertNotEquals(0, response.getTotalProductPrice());
        Assertions.assertNotEquals(0, response.getDeliveryFee());
        Assertions.assertNotEquals(0, response.getTotalPrice());
        Assertions.assertFalse(response.getOrderNum().isBlank());
        Assertions.assertFalse(response.getPaymentType().isBlank());
        Assertions.assertNotNull(response.getPaymentInfo());
        Assertions.assertFalse(response.getCustomerName().isBlank());

        Assertions.assertFalse(response.getProducts().isEmpty());
        response.getProducts().forEach(product -> {
            Assertions.assertFalse(product.getName().isBlank());
            Assertions.assertNotEquals(0, product.getQuantity());
            Assertions.assertNotEquals(0, product.getPrice());
            Assertions.assertFalse(product.getProductUrl().isBlank());
            Assertions.assertFalse(product.getOptions().isEmpty());
        });
    }

    @Test
    void test_retrieveOrderDetailByAdmin_throw_InvalidRoleException() {
        final String ROLE_USER = "USER";
        final String ORDER_ID = "GORDER-000";

        Assertions.assertThrows(
                InvalidRoleException.class,
                () -> orderRetrieveService.retrieveOrderDetailByAdmin(ROLE_USER, ORDER_ID)
        );
    }

    @Test
    void test_retrieveOrderDetailByAdmin_throw_CannotAccessOrderException() {
        final String ROLE_ADMIN = "ADMIN";
        final String ORDER_ID = "GORDER-00x";

        Assertions.assertThrows(
                CannotAccessOrderException.class,
                () -> orderRetrieveService.retrieveOrderDetailByAdmin(ROLE_ADMIN, ORDER_ID)
        );
    }

}