package com.liberty52.product.service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "option_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionDetail {
    @Id
    private String id = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Builder
    private OptionDetail(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public static OptionDetail create(String name, Integer price) {
        return builder().name(name).price(price).build();
    }

    public void associate(ProductOption productOption) {
        this.productOption = productOption;
        this.productOption.addDetail(this);
    }
}