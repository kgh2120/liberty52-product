package com.liberty52.product.service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductState state;

    @Column(nullable = false)
    private Long price;

    @OneToMany(mappedBy = "product")
    private List<ProductOption> productOptions = new ArrayList<>();

    @Builder
    private Product(String name, ProductState state, Long price) {
        this.name = name;
        this.state = state;
        this.price = price;
    }

    public void addOption(ProductOption productOption) {
        this.productOptions.add(productOption);
    }

    public static Product create(String name, ProductState state, Long price) {
        return builder().name(name)
                .state(state)
                .price(price)
                .build();
    }
}
