package com.liberty52.product.service.utils;

import com.liberty52.product.service.entity.*;

public class MockFactory {
    public static CustomProduct createCartItem(String imageUrl, int ea, String authId) {
        return CustomProduct.create(imageUrl, ea, authId);
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
}
