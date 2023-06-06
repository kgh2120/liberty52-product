package com.liberty52.product.service.utils;

import com.liberty52.product.service.controller.dto.OrderDetailRetrieveResponse;
import com.liberty52.product.service.controller.dto.OrderRetrieveProductResponse;
import com.liberty52.product.service.controller.dto.OrdersRetrieveResponse;
import com.liberty52.product.service.controller.dto.ReviewRetrieveResponse;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.entity.payment.BankType;
import com.liberty52.product.service.entity.payment.VBank;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.liberty52.product.service.utils.MockConstants.*;

public class MockFactory {
    public static CustomProduct createCustomProduct(String imageUrl, int quantity, String authId) {
        return CustomProduct.create(imageUrl, quantity, authId);
    }

    public static Product createProduct(String name, ProductState state, Long price) {
        return Product.create(name, state, price);
    }

    public static ProductOption createProductOption(String name, boolean require) {
        return ProductOption.create(name, require, true);
    }

    public static OptionDetail createOptionDetail(String name, Integer price) {
        return OptionDetail.create(name, price, true);
    }

    public static CustomProductOption createProductCartOption() {
        return CustomProductOption.create();
    }

    public static Cart createCart(String authId) {
        return Cart.create(authId);
    }

    public static Orders createOrder(String authId, List<CustomProduct> customProducts) {
        OrderDestination orderDestination = OrderDestination.create("", "", "", "", "", "");
        return Orders.create(authId, 0,orderDestination);
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
        return new OrderRetrieveProductResponse(
                MOCK_CUSTOM_PRODUCT_ID,
                MOCK_PRODUCT_NAME
                ,MOCK_QUANTITY
                ,MOCK_PRICE,
                MOCK_PRODUCT_REPRESENT_URL,
                false,
                null);
    }

    public static OrderDetailRetrieveResponse createMockOrderDetailRetrieveResponse(){
        return new OrderDetailRetrieveResponse(MOCK_ORDER_ID,LocalDate.now().toString(),
                MOCK_ORDER_STATUS_ORDERED.name(),MOCK_ADDRESS,MOCK_RECEIVER_NAME,MOCK_RECEIVER_EMAIL
                ,MOCK_RECEIVER_PHONE_NUMBER,
                MOCK_DELIVERY_FEE,createMockOrderRetrieveProductResponseList());
    }

    public static ReviewRetrieveResponse createMockReviewRetrieveResponse(){
        return new ReviewRetrieveResponse(List.of(createMockReview()), 1,1,1,1, MOCK_AUTH_ID);
    }

    public static Review createMockReview(){
        Review review = Review.create(3, "good");
        review.associate(createCustomProduct(MOCK_PRODUCT_REPRESENT_URL, 1, MOCK_AUTH_ID));

        ReviewImage.create(review,MOCK_PRODUCT_REPRESENT_URL);

        for(int i = 0; i<3; i++){
            Reply reply = Reply.create("맛있따"+i,MOCK_AUTH_ID);
            reply.associate(review);
        }

        return review;
    }

    public static VBank mockVBank() throws Exception {
        VBank vBank = VBank.of(BankType.HANA, "m_account", "m_holder");
        Field id = vBank.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(vBank, "m_vBankId");
        id.setAccessible(false);
        return vBank;
    }

    public static DeliveryOption mockDeliveryOptionOnlyFee(int fee) throws Exception {
        DeliveryOption option = DeliveryOption.feeOf(fee);
        Field id = option.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(option, 1L);
        id.setAccessible(false);
        return option;
    }


}
