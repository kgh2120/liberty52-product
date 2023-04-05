package com.liberty52.product.service.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class CartItem {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String image_url;

    @Column(nullable = false)
    private int ea;

    @Column(nullable = false)
    private String authId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "cartItem")
    private List<ProductCartOption> options = new ArrayList<>();

    public void associate(Product product) {
        this.product = product;
    }

    public void addCartOption(ProductCartOption productCartOption) {
        this.options.add(productCartOption);
    }
}
