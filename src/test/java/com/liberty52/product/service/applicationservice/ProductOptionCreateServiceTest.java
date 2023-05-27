package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.service.controller.dto.CreateOptionDetailRequestDto;
import com.liberty52.product.service.controller.dto.CreateProductOptionRequestDto;
import com.liberty52.product.service.entity.OptionDetail;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.ProductOption;
import com.liberty52.product.service.repository.ProductOptionRepository;
import com.liberty52.product.service.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.liberty52.product.global.constants.RoleConstants.ADMIN;

@SpringBootTest
@Transactional
public class ProductOptionCreateServiceTest {

    @Autowired
    ProductOptionCreateService productOptionCreateService;

    @Autowired
    ProductOptionRepository productOptionRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    void 옵션추가(){
        String name = "테스트옵션";

        Product product = productRepository.findByName("Liberty 52_Frame").orElseGet(null);

        CreateProductOptionRequestDto createOptionDetailRequestDto = CreateProductOptionRequestDto.create(name, false, true);
        productOptionCreateService.createProductOptionByAdmin(ADMIN, createOptionDetailRequestDto, product.getId());

        Product testProduct = productRepository.findByName("Liberty 52_Frame").orElseGet(null);
        ProductOption productOption = testProduct.getProductOptions().get(testProduct.getProductOptions().size() -1);

        Assertions.assertEquals(productOption.getName(), name);
        Assertions.assertEquals(productOption.isRequire(), false);
        Assertions.assertEquals(productOption.isOnSale(), true);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> productOptionCreateService.createProductOptionByAdmin(ADMIN, createOptionDetailRequestDto, "null"));

    }
}
