package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.forbidden.NotYourResourceException;
import com.liberty52.product.global.exception.external.forbidden.UnRemovableResourceException;
import com.liberty52.product.service.controller.dto.CartItemListRemoveRequestDto;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.repository.*;
import com.liberty52.product.service.utils.MockFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

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
    private CustomProductOptionRepository customProductOptionRepository;
    String authId = "auth_id";
    String imageUrl = "url";
    Product product;
    OptionDetail detailEasel;
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
        customProductOption = customProductOptionRepository.save(customProductOption);

        this.cartItemId = customProduct.getId();
        this.product = product;
        this.detailEasel = detailEasel;
    }

    private CustomProduct createMockCartAndGetItem() {
        Cart cart = cartRepository.findByAuthId(authId).orElseGet(() -> cartRepository.save(MockFactory.createCart(authId)));
        CustomProduct customProduct = customProductRepository.findById(this.cartItemId).get();
        customProduct.associateWithCart(cart);
        return customProductRepository.save(customProduct);
    }
    private CustomProduct createMockOrderAndGetItem() {
        CustomProduct customProduct = customProductRepository.findById(this.cartItemId).get();
        Orders order = ordersRepository.save(MockFactory.createOrder(authId, List.of(customProduct)));
        customProduct.associateWithOrder(order);
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
    @Test
    void removeOrderItemMustThrow() {
        createMockOrderAndGetItem();
        Assertions.assertThrows(UnRemovableResourceException.class, () -> cartItemRemoveService.removeCartItem(authId, cartItemId));
    }

    @Test
    void forbiddenCausedByInvalidAuthId() {
        createMockCartAndGetItem();
        Assertions.assertThrows(NotYourResourceException.class, () -> cartItemRemoveService.removeCartItem(UUID.randomUUID().toString(), cartItemId));
    }

    @Test
    void removeCartItemList() {
        List<CustomProduct> cartItems = IntStream.range(0, 10).mapToObj(i -> createCartItem()).toList();
        List<String> optionIds = cartItems.stream().map(CustomProduct::getOptions).flatMap(Collection::stream).map(CustomProductOption::getId).toList();
        List<String> ids = cartItems.stream().map(CustomProduct::getId).toList();
        Assertions.assertEquals(ids.size(), customProductRepository.findAllById(ids).size());

        cartItemRemoveService.removeCartItemList(authId, CartItemListRemoveRequestDto.createForTest(ids));

        Assertions.assertEquals(0, customProductRepository.findAllById(ids).size());
        Assertions.assertEquals(0, customProductOptionRepository.findAllById(optionIds).size());
    }

    @Test
    void removeCartItemList_NotYourResource() {
        List<String> ids = IntStream.range(0, 10).mapToObj(i -> createCartItem().getId()).toList();
        Assertions.assertThrows(NotYourResourceException.class, () -> cartItemRemoveService.removeCartItemList(UUID.randomUUID().toString(), CartItemListRemoveRequestDto.createForTest(ids)));
    }

    @Test
    void removeCartItemList_UnRemovableResource() {
        List<String> ids = new ArrayList<>(IntStream.range(0, 10).mapToObj(i -> createCartItem().getId()).toList());
        ids.add(createMockOrderAndGetItem().getId());
        Assertions.assertThrows(UnRemovableResourceException.class, () -> cartItemRemoveService.removeCartItemList(authId, CartItemListRemoveRequestDto.createForTest(ids)));
    }

    private CustomProduct createCartItem() {
        CustomProduct customProduct = MockFactory.createCustomProduct(imageUrl, 1, authId);
        customProduct.associateWithProduct(product);
        customProduct = customProductRepository.save(customProduct);

        CustomProductOption customProductOption = MockFactory.createProductCartOption();
        customProductOption.associate(customProduct);
        customProductOption.associate(detailEasel);
        customProductOption = customProductOptionRepository.save(customProductOption);

        Cart cart = cartRepository.findByAuthId(authId).orElseGet(() -> cartRepository.save(MockFactory.createCart(authId)));
        customProduct.associateWithCart(cart);
        return customProductRepository.save(customProduct);
    }

}