package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartItemRequest {

    @NotBlank
    String productId;

    @NotNull
    Integer ea;

    @NotNull
    List<OptionRequest> optionRequestList;


    public void create(String productId, Integer ea) {
        this.productId = productId;
        this.ea = ea;
        this.optionRequestList = new ArrayList<OptionRequest>();
    }

    public void addOprion(String optionId, String detailId){
        OptionRequest optionRequest = new OptionRequest();
        optionRequest.optionId = optionId;
        optionRequest.detailId = detailId;
        this.optionRequestList.add(optionRequest);
    }

    @Getter
    public class OptionRequest {

        @NotBlank
        String optionId;

        @NotBlank
        String detailId;
    }

}
