package com.liberty52.product.service.controller.dto;

import com.liberty52.product.service.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor
public class ProductListResponseDto extends PageDtoBase<ProductListResponseDto.ProductInfo> {

    public ProductListResponseDto(Page<Product> page) {
        super(page, page.getContent().stream().map(ProductInfo::new).toList());
    }

    @Data
    public static class ProductInfo {
        private String id;
        private String name;
        private String state;
        private Long price;

        public ProductInfo(Product p) {
            this.id = p.getId();
            this.name = p.getName();
            this.state = p.getProductState().name();
            this.price = p.getPrice();
        }
    }
}
