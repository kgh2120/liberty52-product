package com.liberty52.product.service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
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

    public void addOption(ProductOption productOption) {
        this.productOptions.add(productOption);
    }

    public void init(String id, String name, ProductState state, Long price) {
        this.id =id;
        this.name = name;
        this.state =state;
        this.price = price;
    }
}
