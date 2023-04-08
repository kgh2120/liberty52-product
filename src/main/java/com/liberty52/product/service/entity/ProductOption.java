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
@Table(name = "product_option")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption {
    @Id
    private String id = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean require;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productOption")
    private List<OptionDetail> optionDetails = new ArrayList<>();

    @Builder
    private ProductOption(String name, boolean require) {
        this.name = name;
        this.require = require;
    }

    public void associate(Product product) {
        this.product = product;
        this.product.addOption(this);
    }

    public void addDetail(OptionDetail optionDetail) {
        this.optionDetails.add(optionDetail);
    }

    public static ProductOption create(String name, boolean require) {
        return builder().name(name).require(require).build();
    }
}