package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.service.controller.dto.ProductInfoRetrieveResponseDto;
import com.liberty52.product.service.controller.dto.ProductOptionDetailResponseDto;
import com.liberty52.product.service.controller.dto.ProductOptionResponseDto;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.repository.ProductRepository;
import com.liberty52.product.service.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.liberty52.product.global.constants.RoleConstants.ADMIN;

@SpringBootTest
@Transactional
public class ProductInfoRetrieveServiceTest {

    @Autowired
    ProductInfoRetrieveService productInfoRetrieveService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @BeforeEach
    void beforeEach() {

    }

    @Test
    void 상품옵션조회(){
        Product product = productRepository.findById("LIB-001").orElseGet(null);

        List<ProductOptionResponseDto> productOptionResponseDtoList=productInfoRetrieveService.retrieveProductOptionInfoList("LIB-001");
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productInfoRetrieveService.retrieveProductOptionInfoList("null"));

        Assertions.assertEquals(productOptionResponseDtoList.size(), product.getProductOptions().size());
        ProductOptionResponseDto optionDto = productOptionResponseDtoList.get(0);
        ProductOption productOption = product.getProductOptions().get(0);

        Assertions.assertEquals(optionDto.getOptionName(), productOption.getName());
        Assertions.assertEquals(optionDto.isOnSail(), productOption.isOnSale());
        Assertions.assertEquals(optionDto.isRequire(), productOption.isRequire());

        Assertions.assertEquals(optionDto.getOptionDetailList().size(), productOption.getOptionDetails().size());
        ProductOptionDetailResponseDto detailDto = optionDto.getOptionDetailList().get(0);
        OptionDetail optionDetail = productOption.getOptionDetails().get(0);

        Assertions.assertEquals(detailDto.getOptionDetailName(), optionDetail.getName());
        Assertions.assertEquals(detailDto.isOnSail(), optionDetail.isOnSale());
        Assertions.assertEquals(detailDto.getPrice(), optionDetail.getPrice());

    }

    @Test
    void 상품조회(){
        List<ProductInfoRetrieveResponseDto> dtoList = productInfoRetrieveService.retrieveProductListByAdmin(ADMIN);


        Product product = productRepository.findByName("Liberty 52_Frame").orElseGet(null);
        List<Review> reviewList = reviewRepository.findByCustomProduct_Product(product);
        int sum = 0;
        for(Review review : reviewList){
            sum= sum+review.getRating();
        }

        Assertions.assertEquals(dtoList.size(), 1);
        ProductInfoRetrieveResponseDto dto = dtoList.get(0);
        Assertions.assertEquals(dto.getId(), "LIB-001");
        Assertions.assertEquals(dto.getPictureUrl(), null);
        Assertions.assertEquals(dto.getName(), "Liberty 52_Frame");
        Assertions.assertEquals(dto.getPrice(), 100);
        Assertions.assertEquals(dto.getMeanRating(), sum/reviewList.size());
        Assertions.assertEquals(dto.getRatingCount(), reviewList.size());
        Assertions.assertEquals(dto.getState(), ProductState.ON_SAIL);
    }

    @Test
    void 단일상품조회(){
        Product product = productRepository.findByName("Liberty 52_Frame").orElseGet(null);
        List<Review> reviewList = reviewRepository.findByCustomProduct_Product(product);
        int sum = 0;
        for(Review review : reviewList){
            sum= sum+review.getRating();
        }
        ProductInfoRetrieveResponseDto dto = productInfoRetrieveService.retrieveProductByAdmin(ADMIN, product.getId());
        Assertions.assertEquals(dto.getId(), "LIB-001");
        Assertions.assertEquals(dto.getPictureUrl(), null);
        Assertions.assertEquals(dto.getName(), "Liberty 52_Frame");
        Assertions.assertEquals(dto.getPrice(), 100);
        Assertions.assertEquals(dto.getMeanRating(), sum/reviewList.size());
        Assertions.assertEquals(dto.getRatingCount(), reviewList.size());
        Assertions.assertEquals(dto.getState(), ProductState.ON_SAIL);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productInfoRetrieveService.retrieveProductByAdmin(ADMIN, "null"));

    }
}
