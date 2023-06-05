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

    /**
     * Order 이후, optionDetail 값은 null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private OptionDetail optionDetail;
    private int price;
    private String optionName;
    private String detailName;

    public static CustomProductOption create() {
        return new CustomProductOption();
    }

    public void associate(CustomProduct customProduct) {
        this.customProduct = customProduct;
        this.customProduct.addOption(this);
    }

    public void associate(OptionDetail optionDetail) {
        this.optionDetail = optionDetail;
    }

    /**
     * 주문이 완료된 후 기존의 옵션-옵션 디테일 정보를
     * 필드로 저장하고, 옵션 디테일과의 연관 관계를 끊는다.
     *
     */
    public void setOptionDetailAndDissociate(){
        price = optionDetail.getPrice();
        detailName = optionDetail.getName();
        optionName = optionDetail.getProductOption().getName();
        this.dissociateOptionDetail();
    }

    private void dissociateOptionDetail() {
        this.optionDetail = null;
    }

}