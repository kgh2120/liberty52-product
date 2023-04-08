package com.liberty52.product.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCartOption {
    @Id
    private String id = UUID.randomUUID().toString();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CartItem cartItem;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProductOption productOption;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private OptionDetail optionDetail;

    public static ProductCartOption create() {
        return new ProductCartOption();
    }

    public void associate(CartItem cartItem) {
        this.cartItem = cartItem;
        this.cartItem.addCartOption(this);
    }

    public void associate(ProductOption productOption) {
        this.productOption = productOption;
    }

    public void associate(OptionDetail optionDetail) {
        this.optionDetail = optionDetail;
    }
}
