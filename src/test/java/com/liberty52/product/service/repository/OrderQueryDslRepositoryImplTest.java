package com.liberty52.product.service.repository;


import static com.liberty52.product.global.contants.RepresentImageUrl.LIBERTY52_FRAME_REPRESENTATIVE_URL;
import static com.liberty52.product.service.utils.MockConstants.*;
import static com.liberty52.product.service.utils.MockFactory.*;
import static com.liberty52.product.service.utils.TestInitiator.initDataForTestingOrder;
import static org.assertj.core.api.Assertions.*;

import com.liberty52.product.service.controller.dto.OrderDetailRetrieveResponse;
import com.liberty52.product.service.controller.dto.OrdersRetrieveResponse;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.OrderDestination;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.ProductState;
import com.liberty52.product.service.utils.TestInitiator;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderQueryDslRepositoryImplTest {


    @Autowired
    EntityManager em;

    OrderQueryDslRepositoryImpl orderQueryDslRepositoryImpl;
    String orderId;

    @BeforeEach
    void beforeEach(){
        orderQueryDslRepositoryImpl = new OrderQueryDslRepositoryImpl(em);
        orderId = initDataForTestingOrder(em);
    }

    @AfterEach
    void afterEach(){
        em.clear();
        em.flush();
    }



    @Test
    void retrieveOrderTest  () throws Exception{

        //given
        List<OrdersRetrieveResponse> responses = orderQueryDslRepositoryImpl.retrieveOrders(
                MOCK_AUTH_ID);
        //when

        //then
//
        assertThat(responses.size()).isSameAs(1);
        assertThat(responses.get(0).getOrderDate()).isEqualTo(LocalDate.now().toString());
        assertThat(responses.get(0).getReceiverEmail()).isEqualTo(MOCK_RECEIVER_EMAIL);
        assertThat(responses.get(0).getReceiverPhoneNumber()).isEqualTo(MOCK_RECEIVER_PHONE_NUMBER);
        assertThat(responses.get(0).getReceiverName()).isEqualTo(MOCK_RECEIVER_NAME);
    }

    @Test
    void retrieveOrderDetailTest () throws Exception{
        //given   //when
        OrderDetailRetrieveResponse response = orderQueryDslRepositoryImpl.retrieveOrderDetail(
                MOCK_AUTH_ID, orderId).get();
       //then
        assertThat(response.getOrderId()).isEqualTo(orderId);
        assertThat(response.getOrderDate()).isEqualTo(LocalDate.now().toString());
        assertThat(response.getDeliveryFee()).isEqualTo(0);
        assertThat(response.getOrderStatus()).isEqualTo(MOCK_ORDER_STATUS_ORDERED.name());
        assertThat(response.getTotalPrice()).isEqualTo(MOCK_PRICE);
        assertThat(response.getTotalProductPrice()).isEqualTo(MOCK_PRICE);
        assertThat(response.getReceiverName()).isEqualTo(MOCK_RECEIVER_NAME);
        assertThat(response.getReceiverEmail()).isEqualTo(MOCK_RECEIVER_EMAIL);
        assertThat(response.getReceiverPhoneNumber()).isEqualTo(MOCK_RECEIVER_PHONE_NUMBER);
        assertThat(response.getProductRepresentUrl()).isEqualTo(LIBERTY52_FRAME_REPRESENTATIVE_URL);

        assertThat(response.getAddress()).isEqualTo(MOCK_ADDRESS+" "+MOCK_ADDRESS);
        assertThat(response.getProducts().get(0).getName()).isEqualTo(MOCK_PRODUCT_NAME);
        assertThat(response.getProducts().get(0).getPrice()).isEqualTo(MOCK_PRICE);
        assertThat(response.getProducts().get(0).getQuantity()).isEqualTo(MOCK_QUANTITY);
        assertThat(response.getProducts().get(0).getProductUrl()).isEqualTo(MOCK_PRODUCT_REPRESENT_URL);





    }
}