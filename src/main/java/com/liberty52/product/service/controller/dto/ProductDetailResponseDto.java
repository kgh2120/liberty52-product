package com.liberty52.product.service.controller.dto;

import com.liberty52.product.service.entity.OptionDetail;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.ProductOption;
import lombok.Data;

import java.util.List;

@Data
public class ProductDetailResponseDto {
    private String id;
    private String name;
    private String state;
    private Long price;
    private List<ProductOptionDto> options;

    public ProductDetailResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.state = product.getState().name();
        this.price = product.getPrice();
        this.options = product.getProductOptions().stream().filter(ProductOption::isOnSale).map(ProductOptionDto::new).toList();
    }

    @Data
    public static class ProductOptionDto {
        private String id;
        private String name;
        private boolean require;
        private List<OptionDetailDto> optionItems;

        public ProductOptionDto(ProductOption op) {
            this.id = op.getId();
            this.name = op.getName();
            this.require = op.isRequire();
            this.optionItems = op.getOptionDetails().stream().filter(OptionDetail::isOnSale).map(OptionDetailDto::new).toList();
        }
        @Data
        public static class OptionDetailDto {
            private String id;
            private String name;
            private Integer price;

            public OptionDetailDto(OptionDetail detail) {
                this.id = detail.getId();
                this.name = detail.getName();
                this.price = detail.getPrice();
            }
        }
    }
}
