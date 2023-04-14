package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.OptionDetailNotFoundException;
import com.liberty52.product.global.exception.external.OptionNotFoundException;
import com.liberty52.product.global.exception.external.ProductNotFoundException;
import com.liberty52.product.service.controller.dto.CartItemRequest;
import com.liberty52.product.service.entity.Cart;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.CustomProductOption;
import com.liberty52.product.service.repository.CartItemRepository;
import com.liberty52.product.service.repository.CartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class CartItemCreateServiceTest {

    @Autowired
    CartItemCreateService cartItemCreateService;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartRepository cartRepository;

    @Test
    void 장바구니생성() {
        cartItemCreateService.init();

        CartItemRequest dto1 = new CartItemRequest();
        String[] option = {"이젤 거치형", "1mm 두께 승화전사 인쇄용 알루미늄시트", "무광실버"};
        dto1.create("Liberty52", 1, option);


        CartItemRequest dto2 = new CartItemRequest();
        dto2.create("L", 2, option);

        String[] optionErr = {"벽걸이형", "1mm 두께 승화전사 인쇄용 알루미늄시트", "무광레드"};
        CartItemRequest dto3 = new CartItemRequest();
        dto3.create("Liberty52", 4, optionErr);

        cartItemCreateService.createCartItem("aaa", null, dto1);
        Cart cart = cartRepository.findByAuthId("aaa").orElseThrow(() ->new RuntimeException());


        CustomProduct cartItem = cart.getCustomProducts().get(0);
        Assertions.assertEquals(cartItem.getQuantity(), 1);
        Assertions.assertEquals(cartItem.getAuthId(), "aaa");
        Assertions.assertEquals(cartItem.getProduct().getName(), "Liberty52");


        List<CustomProductOption> productCartOptionList = cartItem.getOptions();
        System.out.println(cartItem.getOptions().size());
        Assertions.assertEquals(productCartOptionList.get(0).getOptionDetail().getName(), "이젤 거치형");
        Assertions.assertEquals(productCartOptionList.get(1).getOptionDetail().getName(), "1mm 두께 승화전사 인쇄용 알루미늄시트");
        Assertions.assertEquals(productCartOptionList.get(2).getOptionDetail().getName(), "무광실버");

        Assertions.assertThrows(ProductNotFoundException.class, () -> cartItemCreateService.createCartItem("aaa", null, dto2));


        Assertions.assertThrows(ProductNotFoundException.class, () -> cartItemCreateService.createCartItem("aaa", null, dto2));

    }
}
