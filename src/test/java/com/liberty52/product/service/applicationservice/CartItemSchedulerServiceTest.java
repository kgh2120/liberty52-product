package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.CartItemRequest;
import com.liberty52.product.service.entity.Cart;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.CustomProductOption;
import com.liberty52.product.service.event.internal.ImageRemovedEvent;
import com.liberty52.product.service.event.internal.dto.ImageRemovedEventDto;
import com.liberty52.product.service.repository.CartRepository;
import com.liberty52.product.service.repository.CustomProductOptionRepository;
import com.liberty52.product.service.repository.CustomProductRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class CartItemSchedulerServiceTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CustomProductRepository customProductRepository;

    @Autowired
    CustomProductOptionRepository customProductOptionRepository;

    @Autowired
    CartItemCreateService cartItemCreateService;

    @Autowired
    CartItemSchedulerService cartItemSchedulerService;

    @Autowired
    EntityManager entityManager;

    List<String> productID = new ArrayList<>();
    List<String> optionId = new ArrayList<>();
    String authId = "guest";

    @BeforeEach
    void init() throws IOException {
        CartItemRequest dto1 = new CartItemRequest();
        String[] option1 = {"이젤 거치형", "1mm 두께 승화전사 인쇄용 알루미늄시트", "무광실버"};
        dto1.create("Liberty 52_Frame", 1, option1);

        CartItemRequest dto2 = new CartItemRequest();
        String[] option2 = {"벽걸이형", "1mm 두께 승화전사 인쇄용 알루미늄시트", "유광백색"};
        dto2.create("Liberty 52_Frame", 2, option2);

        MockMultipartFile imageFile = new MockMultipartFile("image", "test.png", "image/jpeg", new FileInputStream("src/test/resources/static/test.jpg"));

        cartItemCreateService.createGuestCartItem(authId, imageFile, dto1);
        cartItemCreateService.createGuestCartItem(authId, imageFile, dto2);

        Cart cart = cartRepository.findByAuthId(authId).orElse(null);

        for(CustomProduct customProduct : cart.getCustomProducts()){
            customProduct.getOptions().forEach(option -> optionId.add(option.getId()) );
            productID.add(customProduct.getId());
        }

        cart.updateExpiryDate(LocalDate.now().minusDays(7));
        cartRepository.save(cart);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void 스케줄러삭제() throws IOException {
        Cart beforeCart = cartRepository.findByAuthId(authId).orElse(null);
        Assertions.assertEquals(beforeCart.getAuthId(), authId);
        cartItemSchedulerService.deleteNonMemberCart();
        Cart afterCart = cartRepository.findByAuthId(authId).orElse(null);
        //Cart afterCart = cartRepository.findById(beforeCart.getId()).orElse(null);
        Assertions.assertNull(afterCart);
        for(String product : productID){
            CustomProduct customProduct= customProductRepository.findById(product).orElse(null);
            Assertions.assertNull(customProduct);
        }
        for(String option : optionId){
            CustomProductOption customProductOption = customProductOptionRepository.findById(option).orElse(null);
            Assertions.assertNull(customProductOption);
        }

        //Cart afterCart = cartRepository.findByAuthId(authId).orElse(null);
        //Assertions.assertNull(afterCart);


    }

}
