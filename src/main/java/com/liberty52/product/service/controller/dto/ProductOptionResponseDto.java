package com.liberty52.product.service.controller.dto;

import com.liberty52.product.service.entity.OptionDetail;
import com.liberty52.product.service.entity.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionResponseDto {

    String optionId;
    String optionName;
    boolean require;
    boolean onSale;
    List<ProductOptionDetailResponseDto> optionDetailList;

    public ProductOptionResponseDto(ProductOption productOption, boolean onSale) {
        this.optionId = productOption.getId();
        this.optionName = productOption.getName();
        this.require = productOption.isRequire();
        this.onSale = productOption.isOnSale();

        if(productOption.getOptionDetails().size() == 0){
            optionDetailList = Collections.emptyList();
        }

        if(onSale){
            optionDetailList = productOption.getOptionDetails().stream().filter(OptionDetail::isOnSale).map(ProductOptionDetailResponseDto::new).collect(Collectors.toList());

        } else{
            optionDetailList = productOption.getOptionDetails().stream().sorted(Comparator.comparing(OptionDetail::isOnSale).reversed()).map(ProductOptionDetailResponseDto::new).collect(Collectors.toList());

        }
    }

}
