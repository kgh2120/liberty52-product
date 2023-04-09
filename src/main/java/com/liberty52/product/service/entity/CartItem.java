package com.liberty52.product.service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bouncycastle.math.ec.ECAlgorithms;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int ea;

    @Column(nullable = false, updatable = false)
    private String authId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "cartItem")
    private List<ProductCartOption> options;

    public static CartItem createCartItem(String authId, int ea, String image) {
        return new CartItem(image, ea, authId);
    }

    @Builder
    private CartItem(String imageUrl, int ea, String authId) {
        this.imageUrl = imageUrl;
        this.ea = ea;
        this.authId = authId;
        this.options = new ArrayList<>();
    }

    public static CartItem create(String imageUrl, int ea, String authId) {
        return builder().imageUrl(imageUrl).ea(ea).authId(authId).build();
    }

    public void associate(Product product) {
        this.product = product;
    }

    public void addCartOption(ProductCartOption productCartOption) {
        this.options.add(productCartOption);
    }
}
