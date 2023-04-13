package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.OptionDetailNotFoundException;
import com.liberty52.product.global.exception.external.OptionNotFoundException;
import com.liberty52.product.global.exception.external.ProductNotFoundException;
import com.liberty52.product.service.controller.dto.CartItemRequest;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.CustomProductOption;
import com.liberty52.product.service.repository.CartItemRepository;
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

    @Test
    void 장바구니생성(){
        cartItemCreateService.init();

        CartItemRequest dto1 = new CartItemRequest();
        dto1.create("L1", 1);
        dto1.addOprion("a","a1");
        dto1.addOprion("b","b1");
        dto1.addOprion("c","c2");

        CartItemRequest dto2 = new CartItemRequest();
        dto2.create("L", 2);
        dto2.addOprion("a","a1");

        CartItemRequest dto3 = new CartItemRequest();
        dto3.create("L1", 3);
        dto3.addOprion("d","a1");

        CartItemRequest dto4 = new CartItemRequest();
        dto4.create("L1", 4);
        dto4.addOprion("a","a5");

        cartItemCreateService.createCartItem("aaa", null, dto1);
        List<CustomProduct> cartItemList = cartItemRepository.findByAuthId("aaa");
        CustomProduct cartItem = cartItemList.get(0);
        Assertions.assertEquals(cartItem.getQuantity(), 1);
        Assertions.assertEquals(cartItem.getAuthId(), "aaa");
        Assertions.assertEquals(cartItem.getProduct().getId(), "L1");


        List<CustomProductOption> productCartOptionList = cartItem.getOptions();
        System.out.println(cartItem.getOptions().size());
//        Assertions.assertEquals(productCartOptionList.get(0).getProductOption().getId(),"a");
        Assertions.assertEquals(productCartOptionList.get(0).getOptionDetail().getId(),"a1");
//        Assertions.assertEquals(productCartOptionList.get(1).getProductOption().getId(),"b");
        Assertions.assertEquals(productCartOptionList.get(1).getOptionDetail().getId(),"b1");
//        Assertions.assertEquals(productCartOptionList.get(2).getProductOption().getId(),"c");
        Assertions.assertEquals(productCartOptionList.get(2).getOptionDetail().getId(),"c2");

        Assertions.assertThrows(ProductNotFoundException.class, () -> cartItemCreateService.createCartItem("aaa", null, dto2));

        Assertions.assertThrows(OptionNotFoundException.class, () -> cartItemCreateService.createCartItem("aaa", null, dto3));

        Assertions.assertThrows(OptionDetailNotFoundException.class, () -> cartItemCreateService.createCartItem("aaa", null, dto4));

    }
}
