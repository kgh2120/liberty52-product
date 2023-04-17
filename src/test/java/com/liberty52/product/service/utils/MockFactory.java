package com.liberty52.product.service.utils;

import static com.liberty52.product.service.utils.MockConstants.*;

import com.liberty52.product.service.controller.dto.OrderDetailRetrieveResponse;
import com.liberty52.product.service.controller.dto.OrderRetrieveProductResponse;
import com.liberty52.product.service.controller.dto.OrdersRetrieveResponse;
import com.liberty52.product.service.entity.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockFactory {
    public static CustomProduct createCustomProduct(String imageUrl, int quantity, String authId) {
        return CustomProduct.create(imageUrl, quantity, authId);
    }

    public static Product createProduct(String name, ProductState state, Long price) {
        return Product.create(name, state, price);
    }

    public static ProductOption createProductOption(String name, boolean require) {
        return ProductOption.create(name, require);
    }

    public static OptionDetail createOptionDetail(String name, Integer price) {
        return OptionDetail.create(name, price);
    }

    public static CustomProductOption createProductCartOption() {
        return CustomProductOption.create();
    }

    public static Cart createCart(String authId) {
        return Cart.create(authId);
    }

    public static Orders createOrder(String authId, List<CustomProduct> customProducts) {
        return Orders.create(authId, 0,null);
    }

    public static List<OrdersRetrieveResponse> createMockOrderRetrieveResponseList(){
        List<OrdersRetrieveResponse> list = new ArrayList<>();
        for (int i = 0; i <MOCK_LIST_SIZE; i++) {
            list.add(createMockOrderRetrieveResponse());
        }
        return list;
    }

    public static OrdersRetrieveResponse createMockOrderRetrieveResponse(){
        return new OrdersRetrieveResponse(MOCK_ORDER_ID, LocalDate.now().toString(),
                MOCK_ORDER_STATUS_ORDERED.name(),MOCK_ADDRESS,MOCK_RECEIVER_NAME,MOCK_RECEIVER_EMAIL
        ,MOCK_RECEIVER_PHONE_NUMBER, createMockOrderRetrieveProductResponseList());
    }
    public static List<OrderRetrieveProductResponse> createMockOrderRetrieveProductResponseList(){
        List<OrderRetrieveProductResponse> list = new ArrayList<>();
        for (int i = 0; i < MOCK_LIST_SIZE; i++) {
            list.add(createMockOrderRetrieveProductResponse());
        }
        return list;
    }
    public static OrderRetrieveProductResponse createMockOrderRetrieveProductResponse(){
        return new OrderRetrieveProductResponse(MOCK_PRODUCT_NAME,MOCK_QUANTITY,MOCK_PRICE,MOCK_PRODUCT_REPRESENT_URL);
    }

    public static OrderDetailRetrieveResponse createMockOrderDetailRetrieveResponse(){
        return new OrderDetailRetrieveResponse(MOCK_ORDER_ID,LocalDate.now().toString(),
                MOCK_ORDER_STATUS_ORDERED.name(),MOCK_ADDRESS,MOCK_RECEIVER_NAME,MOCK_RECEIVER_EMAIL
                ,MOCK_RECEIVER_PHONE_NUMBER,
                MOCK_TOTAL_PRODUCT_PRICE,MOCK_DELIVERY_FEE,createMockOrderRetrieveProductResponseList());
    }
}
