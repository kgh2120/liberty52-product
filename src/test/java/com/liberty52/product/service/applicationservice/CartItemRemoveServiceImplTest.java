package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.NotYourResource;
import com.liberty52.product.global.exception.external.UnRemovableResourceException;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.repository.*;
import com.liberty52.product.service.utils.MockFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
class CartItemRemoveServiceImplTest {
    @Autowired
    CartItemRemoveService cartItemRemoveService;
    @Autowired
    CartItemRepository customProductRepository;
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
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrdersRepository ordersRepository;

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

        CustomProduct customProduct = MockFactory.createCustomProduct(imageUrl, 1, authId);
        customProduct.associateWithProduct(product);
        customProduct = customProductRepository.save(customProduct);

        CustomProductOption customProductOption = MockFactory.createProductCartOption();
        customProductOption.associate(customProduct);
        customProductOption.associate(detailEasel);
        customProductOption = productCartOptionRepository.save(customProductOption);

        this.cartItemId = customProduct.getId();
    }

    private CustomProduct createMockCartAndGetItem() {
        Cart cart = cartRepository.findByAuthId(authId).orElse(cartRepository.save(MockFactory.createCart(authId)));
        CustomProduct customProduct = customProductRepository.findById(this.cartItemId).get();
        customProduct.associateWithCart(cart);
        return customProductRepository.save(customProduct);
    }
    private CustomProduct createMockOrderAndGetItem() {
        CustomProduct customProduct = customProductRepository.findById(this.cartItemId).get();
        Orders order = ordersRepository.save(MockFactory.createOrder(authId, List.of(customProduct)));
        return customProductRepository.save(customProduct);
    }

    @Test
    void removeCartItem() {
        CustomProduct customProduct = createMockCartAndGetItem();

        Assertions.assertFalse(customProduct.getOptions().isEmpty());

        cartItemRemoveService.removeCartItem(authId, cartItemId);

        customProduct.getOptions().forEach(option -> {
            boolean b = productOptionRepository.existsById(option.getId());
            Assertions.assertFalse(b);
        });

        Assertions.assertFalse(customProductRepository.existsById(cartItemId));

    }

    // Order의 CustomProduct를 삭제하려는 경우 반드시 예외가 발생한다.
//    @Test
    void removeOrderItemMustThrow() {
        createMockOrderAndGetItem();
        Assertions.assertThrows(UnRemovableResourceException.class, () -> cartItemRemoveService.removeCartItem(authId, cartItemId));
    }

    @Test
    void forbiddenCausedByInvalidAuthId() {
        createMockCartAndGetItem();
        Assertions.assertThrows(NotYourResource.class, () -> cartItemRemoveService.removeCartItem(UUID.randomUUID().toString(), cartItemId));
    }
}