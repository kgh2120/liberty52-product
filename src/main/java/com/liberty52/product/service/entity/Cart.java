package com.liberty52.product.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Cart {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(unique = true, nullable = false, updatable = false)
    private String authId;

    @OneToMany(mappedBy = "cart")
    private List<CustomProduct> customProducts = new ArrayList<>();

    public Cart(String authId) {
        this.authId = authId;
    }
    public static Cart create(String authId) {
        return new Cart(authId);
    }

    public void addCustomProduct(CustomProduct customProduct){
        customProducts.add(customProduct);
    }
}
