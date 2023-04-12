package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.repository.*;
import com.liberty52.product.service.utils.MockFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CartItemRemoveServiceImplTest {
    @Autowired
    CartItemRemoveService cartItemRemoveService;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductOptionRepository productOptionRepository;
    @Autowired
    private OptionDetailRepository optionDetailRepository;
    @Autowired
    private CustomProductOptionRepository productCartOptionRepository;
    String authId = "auth_id";
    String imageUrl = "url";

    String cartItemId;

    @BeforeEach
    void beforeEach() {
        Product product = MockFactory.createProduct("Liberty 52_Frame", ProductState.ON_SAIL, 10_000_000L);
        product = productRepository.save(product);

        ProductOption displayOption = MockFactory.createProductOption("거치 방식", true);
        displayOption.associate(product);
        displayOption = productOptionRepository.save(displayOption);

        OptionDetail detailEasel = MockFactory.createOptionDetail("이젤 거치형", 100_000);
        detailEasel.associate(displayOption);
        detailEasel = optionDetailRepository.save(detailEasel);

        OptionDetail detailWall = MockFactory.createOptionDetail("벽걸이형", 80_000);
        detailWall.associate(displayOption);
        detailWall = optionDetailRepository.save(detailWall);

        CustomProduct cartItem = MockFactory.createCartItem(imageUrl, 1, authId);
        cartItem.associateWithProduct(product);
        cartItem = cartItemRepository.save(cartItem);

        CustomProductOption productCartOption = MockFactory.createProductCartOption();
        productCartOption.associate(cartItem);
        productCartOption.associate(displayOption);
        productCartOption.associate(detailEasel);
        productCartOption = productCartOptionRepository.save(productCartOption);

        System.out.println(cartItem);
        System.out.println(cartItem.getOptions());
        System.out.println(cartItem.getOptions().size());
        this.cartItemId = cartItem.getId();
    }

    @Test
    void removeCartItem() {
        CustomProduct cartItem = cartItemRepository.findById(cartItemId).get();

        Assertions.assertFalse(cartItem.getOptions().isEmpty());

        cartItemRemoveService.removeCartItem(authId, cartItemId);

        cartItem.getOptions().forEach(option -> {
            boolean b = productOptionRepository.existsById(option.getId());
            Assertions.assertFalse(b);
        });

        Assertions.assertFalse(cartItemRepository.existsById(cartItemId));
    }
}