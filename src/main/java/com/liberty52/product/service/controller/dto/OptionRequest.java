package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OptionRequest {

    @NotBlank
    String optionId;

    @NotBlank
    String detailId;
}
