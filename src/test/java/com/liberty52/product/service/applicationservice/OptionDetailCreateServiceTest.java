package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.service.controller.dto.CreateOptionDetailRequestDto;
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
public class OptionDetailCreateServiceTest {

    @Autowired
    OptionDetailCreateService optionDetailCreateService;

    @Autowired
    ProductOptionRepository productOptionRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    void 옵션선택지추가(){
        String name = "테스트선택지";

        Product product = productRepository.findByName("Liberty 52_Frame").orElseGet(null);
        String optionId = product.getProductOptions().get(0).getId();

        CreateOptionDetailRequestDto createOptionDetailRequestDto = CreateOptionDetailRequestDto.create(name, 20000, true);
        optionDetailCreateService.createOptionDetailByAdmin(ADMIN, createOptionDetailRequestDto, optionId);

        ProductOption productOption = productOptionRepository.findById(optionId).orElseGet(null);
        OptionDetail optiondetail = productOption.getOptionDetails().get(productOption.getOptionDetails().size() - 1);

        Assertions.assertEquals(optiondetail.getName(), name);
        Assertions.assertEquals(optiondetail.getPrice(), 20000);
        Assertions.assertEquals(optiondetail.isOnSale(), true);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> optionDetailCreateService.createOptionDetailByAdmin(ADMIN, createOptionDetailRequestDto, "null"));

    }
}
