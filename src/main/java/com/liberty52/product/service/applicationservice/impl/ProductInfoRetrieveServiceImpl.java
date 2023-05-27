package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.ProductInfoRetrieveService;
import com.liberty52.product.service.controller.dto.*;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.repository.CartRepository;
import com.liberty52.product.service.repository.ProductRepository;
import com.liberty52.product.service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductInfoRetrieveServiceImpl implements ProductInfoRetrieveService {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final CartRepository cartRepository;

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

        if(productList.size() == 0){
            throw new ResourceNotFoundException("product");
        }

        for (Product product : productList){
            List<Review> productReviewList = reviewList.stream().filter(r -> r.getCustomProduct().getProduct().equals(product)).collect(Collectors.toList());
            float meanRate = product.getRate(productReviewList);
            dto.add(ProductInfoRetrieveResponseDto.of(product.getId(), product.getPictureUrl(), product.getName(), product.getPrice(), meanRate, productReviewList.size(),product.getProductState()));
        }
        return dto;
    }

    @Override
    public ProductInfoRetrieveResponseDto retrieveProductByAdmin(String role, String productId) {
      Validator.isAdmin(role);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product", "id", productId));
        List<Review> productReviewList = reviewRepository.findByCustomProduct_Product(product);
        float meanRate = product.getRate(productReviewList);
        return ProductInfoRetrieveResponseDto.of(product.getId(), product.getPictureUrl(), product.getName(), product.getPrice(), meanRate, productReviewList.size(),product.getProductState());
    }

    @Override
    public List<ProductInfoByCartResponseDto> retrieveProductOptionListByCart(String authId) {
        Cart cart = cartRepository.findByAuthId(authId).orElse(null);
        List<ProductInfoByCartResponseDto> productInfoByCartResponseDtoList = new ArrayList<>();
        if (cart==null || cart.getCustomProducts().size() == 0){
            return productInfoByCartResponseDtoList;
        }

        List<Product> productList = cart.getCustomProducts().stream().map(c->c.getProduct()).distinct().collect(Collectors.toList());
        for(Product product : productList){
            productInfoByCartResponseDtoList.add(new ProductInfoByCartResponseDto(product));
        }

        return productInfoByCartResponseDtoList;
    }

}
