package com.liberty52.product.service.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_option")
@Getter
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productOption", cascade = CascadeType.ALL)
    private List<OptionDetail> optionDetails = new ArrayList<>();

    public void associate(Product product) {
        this.product = product;
        this.product.addOption(this);
    }

    public void addDetail(OptionDetail optionDetail) {
        this.optionDetails.add(optionDetail);
    }
}