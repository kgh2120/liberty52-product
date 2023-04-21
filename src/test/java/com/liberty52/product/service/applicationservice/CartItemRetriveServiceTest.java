package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.CartItemRequest;
import com.liberty52.product.service.controller.dto.CartItemResponse;
import com.liberty52.product.service.controller.dto.CartOptionResponse;
import com.liberty52.product.service.entity.Cart;
import com.liberty52.product.service.repository.CartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class CartItemRetriveServiceTest {

    @Autowired
    CartItemCreateService cartItemCreateService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRetriveService cartItemRetriveService;

    @Test
    void 장바구니조회(){
        Cart cart = Cart.create("ccc");
        cartRepository.save(cart);

        CartItemRequest dto1 = new CartItemRequest();
        String[] option = {"이젤 거치형", "1mm 두께 승화전사 인쇄용 알루미늄시트", "무광실버"};
        dto1.create("Liberty 52_Frame", 1, option);
        cartItemCreateService.createAuthCartItem("aaa", null, dto1);

        CartItemRequest dto2 = new CartItemRequest();
        String[] option2 = {"벽걸이형", "무광백색"};

        dto2.create("Liberty 52_Frame", 2, option2);
        cartItemCreateService.createAuthCartItem("aaa", null, dto2);

        List<CartItemResponse> cartItemResponseList = cartItemRetriveService.retriveCartItem("aaa");
        Assertions.assertEquals(cartItemResponseList.size(), 2);

        CartItemResponse cartItemResponse1 = cartItemResponseList.get(0);
        Assertions.assertEquals(cartItemResponse1.getName(), "Liberty 52_Frame");
        Assertions.assertEquals(cartItemResponse1.getImageUrl(), "");
        Assertions.assertEquals(cartItemResponse1.getPrice(), 10000000);
        Assertions.assertEquals(cartItemResponse1.getQuantity(), 1);

        List<CartOptionResponse> optionRequestList1 = cartItemResponse1.getOptions();
        Assertions.assertEquals(optionRequestList1.size(), 3);
        CartOptionResponse cartOptionResponse11 = optionRequestList1.get(0);
        Assertions.assertEquals(cartOptionResponse11.getOptionName(), "거치 방식");
        Assertions.assertEquals(cartOptionResponse11.getDetailName(), "이젤 거치형");
        Assertions.assertEquals(cartOptionResponse11.getPrice(), 100000);
        Assertions.assertEquals(cartOptionResponse11.isRequire(), true);

        CartOptionResponse cartOptionResponse12 = optionRequestList1.get(1);
        Assertions.assertEquals(cartOptionResponse12.getOptionName(), "기본소재");
        Assertions.assertEquals(cartOptionResponse12.getDetailName(), "1mm 두께 승화전사 인쇄용 알루미늄시트");
        Assertions.assertEquals(cartOptionResponse12.getPrice(), 0);
        Assertions.assertEquals(cartOptionResponse12.isRequire(), true);


        CartOptionResponse cartOptionResponse13 = optionRequestList1.get(2);
        Assertions.assertEquals(cartOptionResponse13.getOptionName(), "기본소재 옵션");
        Assertions.assertEquals(cartOptionResponse13.getDetailName(), "무광실버");
        Assertions.assertEquals(cartOptionResponse13.getPrice(), 400000);
        Assertions.assertEquals(cartOptionResponse13.isRequire(), true);


        CartItemResponse cartItemResponse2 = cartItemResponseList.get(1);
        Assertions.assertEquals(cartItemResponse2.getName(), "Liberty 52_Frame");
        Assertions.assertEquals(cartItemResponse2.getImageUrl(), "");
        Assertions.assertEquals(cartItemResponse2.getPrice(), 10000000);
        Assertions.assertEquals(cartItemResponse2.getQuantity(), 2);

        List<CartOptionResponse> optionRequestList2 = cartItemResponse2.getOptions();
        Assertions.assertEquals(optionRequestList2.size(), 2);

        CartOptionResponse cartOptionResponse21 = optionRequestList2.get(0);
        Assertions.assertEquals(cartOptionResponse21.getOptionName(), "거치 방식");
        Assertions.assertEquals(cartOptionResponse21.getDetailName(), "벽걸이형");
        Assertions.assertEquals(cartOptionResponse21.getPrice(), 200000);
        Assertions.assertEquals(cartOptionResponse21.isRequire(), true);


        CartOptionResponse cartOptionResponse22 = optionRequestList2.get(1);
        Assertions.assertEquals(cartOptionResponse22.getOptionName(), "기본소재 옵션");
        Assertions.assertEquals(cartOptionResponse22.getDetailName(), "무광백색");
        Assertions.assertEquals(cartOptionResponse22.getPrice(), 500000);
        Assertions.assertEquals(cartOptionResponse22.isRequire(), true);

        List<CartItemResponse> cartItemResponseList1 = cartItemRetriveService.retriveCartItem("bbb");
        Assertions.assertEquals(cartItemResponseList1.size(), 0);

        List<CartItemResponse> cartItemResponseList2 = cartItemRetriveService.retriveCartItem("ccc");
        Assertions.assertEquals(cartItemResponseList2.size(), 0);
    }
}
