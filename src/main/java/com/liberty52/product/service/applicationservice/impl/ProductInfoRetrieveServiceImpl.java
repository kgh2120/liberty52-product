package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.ProductInfoRetrieveService;
import com.liberty52.product.service.controller.dto.ProductDetailResponseDto;
import com.liberty52.product.service.controller.dto.ProductInfoRetrieveResponseDto;
import com.liberty52.product.service.controller.dto.ProductListResponseDto;
import com.liberty52.product.service.controller.dto.ProductOptionDetailResponseDto;
import com.liberty52.product.service.controller.dto.ProductOptionResponseDto;
import com.liberty52.product.service.entity.OptionDetail;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.ProductOption;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.repository.ProductRepository;
import com.liberty52.product.service.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductInfoRetrieveServiceImpl implements ProductInfoRetrieveService {

  private final ProductRepository productRepository;
  private final ReviewRepository reviewRepository;

  @Override
  public ProductListResponseDto retrieveProductList(Pageable pageable) {
    return new ProductListResponseDto(productRepository.findAll(pageable));
  }

  @Override
  public ProductDetailResponseDto retrieveProductDetail(String productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("product", "id", productId));
    return new ProductDetailResponseDto(product);
  }

  @Override
  public List<ProductOptionResponseDto> retrieveProductOptionInfoList(String productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("product", "id", productId));
    List<ProductOptionResponseDto> productOptionResponseDtoList = new ArrayList<>();
    for (ProductOption productOption : product.getProductOptions()) {
      productOptionResponseDtoList.add(
          ProductOptionResponseDto.of(productOption.getId(), productOption.getName(),
              productOption.isRequire(), productOption.isOnSale(),
              getOptionDetails(productOption.getOptionDetails())));
    }
    return productOptionResponseDtoList;
  }

  private List<ProductOptionDetailResponseDto> getOptionDetails(
      List<OptionDetail> optionDetailList) {
    List<ProductOptionDetailResponseDto> productOptionDetailResponseDtoList = new ArrayList<>();
    for (OptionDetail optionDetail : optionDetailList) {
      productOptionDetailResponseDtoList.add(
          ProductOptionDetailResponseDto.of(optionDetail.getId(), optionDetail.getName(),
              optionDetail.getPrice(), optionDetail.isOnSale()));
    }
    return productOptionDetailResponseDtoList;

  }

  @Override
  public List<ProductInfoRetrieveResponseDto> retrieveProductListByAdmin(String role) {
    Validator.isAdmin(role);
    List<ProductInfoRetrieveResponseDto> dto = new ArrayList<>();
    List<Product> productList = productRepository.findAll();
    List<Review> reviewList = reviewRepository.findAll();

    if (productList.size() == 0) {
      throw new ResourceNotFoundException("product", "product", "null");
    }

    for (Product product : productList) {
      List<Review> productReviewList = reviewList.stream()
          .filter(r -> r.getCustomProduct().getProduct().equals(product))
          .collect(Collectors.toList());
      float meanRate = product.getRate(productReviewList);
      dto.add(ProductInfoRetrieveResponseDto.of(product.getId(), product.getPictureUrl(),
          product.getName(), product.getPrice(), meanRate, productReviewList.size(),
          product.getProductState()));
    }

    return dto;
  }

  @Override
  public ProductInfoRetrieveResponseDto retrieveProductByAdmin(String role, String productId) {
    Validator.isAdmin(role);
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("product", "id", productId));
    List<Review> productReviewList = reviewRepository.findByCustomProduct_Product(product);
    float meanRate = product.getRate(productReviewList);
    return ProductInfoRetrieveResponseDto.of(product.getId(), product.getPictureUrl(),
        product.getName(), product.getPrice(), meanRate, productReviewList.size(),
        product.getProductState());
  }
}
