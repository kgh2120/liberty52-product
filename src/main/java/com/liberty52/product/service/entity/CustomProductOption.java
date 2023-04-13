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
public class CustomProductOption {
    @Id
    private String id = UUID.randomUUID().toString();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CustomProduct customProduct;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private OptionDetail optionDetail;

    public static CustomProductOption create() {
        return new CustomProductOption();
    }

    public void associate(CustomProduct cartItem) {
        this.customProduct = cartItem;
        this.customProduct.addCartOption(this);
    }

    public void associate(OptionDetail optionDetail) {
        this.optionDetail = optionDetail;
    }
}
