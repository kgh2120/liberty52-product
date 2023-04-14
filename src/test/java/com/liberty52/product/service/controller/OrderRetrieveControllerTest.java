package com.liberty52.product.service.controller;

import static com.liberty52.product.service.utils.MockConstants.*;

import static com.liberty52.product.service.utils.MockFactory.createMockOrderDetailRetrieveResponse;
import static com.liberty52.product.service.utils.MockFactory.createMockOrderRetrieveResponse;
import static com.liberty52.product.service.utils.MockFactory.createMockOrderRetrieveResponseList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.liberty52.product.global.exception.external.CannotAccessOrderException;
import com.liberty52.product.global.exception.external.ErrorResponse;
import com.liberty52.product.global.exception.external.RestExceptionHandler;
import com.liberty52.product.service.applicationservice.OrderRetrieveService;
import com.liberty52.product.service.controller.dto.OrdersRetrieveResponse;
import com.netflix.discovery.converters.Auto;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(value = {OrderRetrieveController.class, RestExceptionHandler.class})
class OrderRetrieveControllerTest {

    @InjectMocks
    OrderRetrieveController orderRetrieveController;

    @MockBean
    OrderRetrieveService orderRetrieveService;

    @MockBean
    RestExceptionHandler exceptionHandler;

    @Autowired
    MockMvc mockMvc;

    final String ORDER_URL = "/product/orders";

    @Test
    void retrieveOrderForList () throws Exception{
        //given
        given(orderRetrieveService.retrieveOrders(MOCK_AUTH_ID))
                .willReturn(createMockOrderRetrieveResponseList());
        //when

        mockMvc.perform(get(ORDER_URL)
                .header(HttpHeaders.AUTHORIZATION,MOCK_AUTH_ID ))
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(MOCK_LIST_SIZE))
                .andExpect(jsonPath("$.[0].orderId").value(MOCK_ORDER_ID))
                .andExpect(jsonPath("$.[0].orderDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.[0].orderStatus").value(MOCK_ORDER_STATUS_ORDERED.name()))
                .andExpect(jsonPath("$.[0].address").value(MOCK_ADDRESS))
                .andExpect(jsonPath("$.[0].receiverEmail").value(MOCK_RECEIVER_EMAIL))
                .andExpect(jsonPath("$.[0].receiverPhoneNumber").value(MOCK_RECEIVER_PHONE_NUMBER))
                .andExpect(jsonPath("$.[0].receiverName").value(MOCK_RECEIVER_NAME))
                .andExpect(jsonPath("$.[0].productRepresentUrl").value(MOCK_PRODUCT_REPRESENT_URL))
                .andExpect(jsonPath("$.[0].products[0].name").value(MOCK_PRODUCT_NAME))
                .andExpect(jsonPath("$.[0].products[0].quantity").value(MOCK_QUANTITY))
                .andExpect(jsonPath("$.[0].products[0].price").value(MOCK_PRICE))
                .andDo(print());
    }

    @Test
    void retrieveOrderDetail () throws Exception{
        //given
        given(orderRetrieveService.retrieveOrderDetail(MOCK_AUTH_ID, MOCK_ORDER_ID))
                .willReturn(createMockOrderDetailRetrieveResponse());

        //when
        mockMvc.perform(get("/product/orders/"+MOCK_ORDER_ID)
                .header(HttpHeaders.AUTHORIZATION,MOCK_AUTH_ID))
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(MOCK_ORDER_ID))
                .andExpect(jsonPath("$.orderDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.orderStatus").value(MOCK_ORDER_STATUS_ORDERED.name()))
                .andExpect(jsonPath("$.address").value(MOCK_ADDRESS))
                .andExpect(jsonPath("$.receiverEmail").value(MOCK_RECEIVER_EMAIL))
                .andExpect(jsonPath("$.receiverPhoneNumber").value(MOCK_RECEIVER_PHONE_NUMBER))
                .andExpect(jsonPath("$.receiverName").value(MOCK_RECEIVER_NAME))
                .andExpect(jsonPath("$.productRepresentUrl").value(MOCK_PRODUCT_REPRESENT_URL))
                .andExpect(jsonPath("$.totalProductPrice").value(MOCK_TOTAL_PRODUCT_PRICE))
                .andExpect(jsonPath("$.deliveryFee").value(MOCK_DELIVERY_FEE))
                .andExpect(jsonPath("$.totalPrice").value(MOCK_TOTAL_PRICE))
                .andExpect(jsonPath("$.products[0].name").value(MOCK_PRODUCT_NAME))
                .andExpect(jsonPath("$.products[0].quantity").value(MOCK_QUANTITY))
                .andExpect(jsonPath("$.products[0].price").value(MOCK_PRICE))
                .andExpect(jsonPath("$.products[0].productUrl").value(MOCK_PRODUCT_REPRESENT_URL))
                .andDo(print());
    }

    @Test
    void retrieveOrderDetail_throw_cannot_access () throws Exception{
        //given
        given(orderRetrieveService.retrieveOrderDetail(MOCK_AUTH_ID, MOCK_ORDER_ID))
                .willThrow(CannotAccessOrderException.class);
        given(exceptionHandler.handleGlobalException(any(),any()))
                .willReturn(
                        ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponse.createErrorResponse(new CannotAccessOrderException(), ORDER_URL+"/"+MOCK_ORDER_ID))
                );

        //when         //then
        mockMvc.perform(get(ORDER_URL+"/"+MOCK_ORDER_ID)
                .header(HttpHeaders.AUTHORIZATION,MOCK_AUTH_ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorName").value("CANNOT_ACCESS_ORDER"))
                .andExpect(jsonPath("$.errorMessage").value("해당 주문에 접근할 수 없습니다."))
                .andDo(print());



    }


}