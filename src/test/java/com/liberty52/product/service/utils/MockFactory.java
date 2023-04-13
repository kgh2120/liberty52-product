package com.liberty52.product.service.utils;

import com.liberty52.product.service.entity.*;

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
        return Orders.create(authId, customProducts);
    }
}
