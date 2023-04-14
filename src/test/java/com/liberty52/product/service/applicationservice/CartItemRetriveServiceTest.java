package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.NotFoundCartItemException;
import com.liberty52.product.global.exception.external.OptionDetailNotFoundException;
import com.liberty52.product.service.controller.dto.CartItemRequest;
import com.liberty52.product.service.controller.dto.CartItemResponse;
import com.liberty52.product.service.controller.dto.CartOptionResponse;
import com.liberty52.product.service.repository.CartItemRepository;
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
    CartItemRepository cartItemRepository;

    @Autowired
    CartItemRetriveService cartItemRetriveService;

//    @Test
    void 장바구니조회(){
        cartItemCreateService.init();
        CartItemRequest dto1 = new CartItemRequest();
        dto1.create("L1", 1);
        dto1.addOprion("a","a1");
        dto1.addOprion("b","b1");
        dto1.addOprion("c","c2");
        cartItemCreateService.createCartItem("aaa", null, dto1);

        CartItemRequest dto2 = new CartItemRequest();
        dto2.create("L1", 2);
        dto2.addOprion("a","a2");
        dto2.addOprion("c","c4");
        cartItemCreateService.createCartItem("aaa", null, dto2);

        List<CartItemResponse> cartItemResponseList = cartItemRetriveService.retriveCartItem("aaa");
        Assertions.assertEquals(cartItemResponseList.size(), 2);

        CartItemResponse cartItemResponse1 = cartItemResponseList.get(0);
        Assertions.assertEquals(cartItemResponse1.getProductId(), "L1");
        Assertions.assertEquals(cartItemResponse1.getProductName(), "Liberty52");
        Assertions.assertEquals(cartItemResponse1.getProductPrice(), 1000000);
        Assertions.assertEquals(cartItemResponse1.getEa(), 1);

        List<CartOptionResponse> optionRequestList1 = cartItemResponse1.getOptionRequestList();
        Assertions.assertEquals(optionRequestList1.size(), 3);
        CartOptionResponse cartOptionResponse11 = optionRequestList1.get(0);
        Assertions.assertEquals(cartOptionResponse11.getOptionId(), "a");
        Assertions.assertEquals(cartOptionResponse11.getOptionName(), "거치 방식 선택");
        Assertions.assertEquals(cartOptionResponse11.getDetailOptionId(), "a1");
        Assertions.assertEquals(cartOptionResponse11.getDetailOptionName(), "이젤 거치형");
        Assertions.assertEquals(cartOptionResponse11.getPrice(), 500000);

        CartOptionResponse cartOptionResponse12 = optionRequestList1.get(1);
        Assertions.assertEquals(cartOptionResponse12.getOptionId(), "b");
        Assertions.assertEquals(cartOptionResponse12.getOptionName(), "기본소재 선택");
        Assertions.assertEquals(cartOptionResponse12.getDetailOptionId(), "b1");
        Assertions.assertEquals(cartOptionResponse12.getDetailOptionName(), "1mm 두께 승화전사 인쇄용 알루미늄시트");
        Assertions.assertEquals(cartOptionResponse12.getPrice(), 0);

        CartOptionResponse cartOptionResponse13 = optionRequestList1.get(2);
        Assertions.assertEquals(cartOptionResponse13.getOptionId(), "c");
        Assertions.assertEquals(cartOptionResponse13.getOptionName(), "기본소재 옵션");
        Assertions.assertEquals(cartOptionResponse13.getDetailOptionId(), "c2");
        Assertions.assertEquals(cartOptionResponse13.getDetailOptionName(), "무광실버");
        Assertions.assertEquals(cartOptionResponse13.getPrice(), 400000);

        CartItemResponse cartItemResponse2 = cartItemResponseList.get(1);
        Assertions.assertEquals(cartItemResponse2.getProductId(), "L1");
        Assertions.assertEquals(cartItemResponse2.getProductName(), "Liberty52");
        Assertions.assertEquals(cartItemResponse2.getProductPrice(), 1000000);
        Assertions.assertEquals(cartItemResponse2.getEa(), 2);

        List<CartOptionResponse> optionRequestList2 = cartItemResponse2.getOptionRequestList();
        Assertions.assertEquals(optionRequestList2.size(), 2);

        CartOptionResponse cartOptionResponse21 = optionRequestList2.get(0);
        Assertions.assertEquals(cartOptionResponse21.getOptionId(), "a");
        Assertions.assertEquals(cartOptionResponse21.getOptionName(), "거치 방식 선택");
        Assertions.assertEquals(cartOptionResponse21.getDetailOptionId(), "a2");
        Assertions.assertEquals(cartOptionResponse21.getDetailOptionName(), "벽걸이형");
        Assertions.assertEquals(cartOptionResponse21.getPrice(), 300000);

        CartOptionResponse cartOptionResponse22 = optionRequestList2.get(1);
        Assertions.assertEquals(cartOptionResponse22.getOptionId(), "c");
        Assertions.assertEquals(cartOptionResponse22.getOptionName(), "기본소재 옵션");
        Assertions.assertEquals(cartOptionResponse22.getDetailOptionId(), "c4");
        Assertions.assertEquals(cartOptionResponse22.getDetailOptionName(), "무광백색");
        Assertions.assertEquals(cartOptionResponse22.getPrice(), 500000);

        Assertions.assertThrows(NotFoundCartItemException.class, ()->cartItemRetriveService.retriveCartItem("bbb"));
    }
}
