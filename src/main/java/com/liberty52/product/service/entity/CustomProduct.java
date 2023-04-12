package com.liberty52.product.service.entity;

import com.liberty52.product.global.exception.external.InvalidQuantityException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomProduct {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String userCustomPictureUrl;
    @Column(nullable = false)
    private String modelingPictureUrl;
    // 작동 과정을 몰라서 nullable은 안넣음.
    private String thumbnailPictureUrl;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, updatable = false)
    private String authId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "customProduct")
    private List<CustomProductOption> options;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;


    public static CustomProduct createCartItem(String authId, int quantity, String image) {
        return new CustomProduct(image, quantity, authId);
    }

    @Builder
    private CustomProduct(String imageUrl, int quantity, String authId) {
        this.userCustomPictureUrl = imageUrl;
        this.quantity = quantity;
        this.authId = authId;
        this.options = new ArrayList<>();
    }

    public static CustomProduct create(String imageUrl, int quantity, String authId) {
        return builder().imageUrl(imageUrl).quantity(quantity).authId(authId).build();
    }

    public void associateWithProduct(Product product) {
        this.product = product;
    }

    public void addToCart(Cart cart){
        this.cart = cart;
        cart.addCustomProduct(this);
    }
    public void addToOrders(Orders orders){
        verifyQuantity();
        removedFromCart();
        this.orders = orders;
    }

    public void addCartOption(CustomProductOption productCartOption) {
        this.options.add(productCartOption);
    }

    private void verifyQuantity(){
        if( quantity <= 0)
            throw new InvalidQuantityException();
    }

    private void removedFromCart() {
        this.cart = null;
    }
}
