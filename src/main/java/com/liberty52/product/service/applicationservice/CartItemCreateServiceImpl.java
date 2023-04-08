package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.adapter.S3Uploader;
import com.liberty52.product.service.controller.dto.CartItemRequest;
import com.liberty52.product.service.controller.dto.OptionRequest;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemCreateServiceImpl implements CartItemCreateService{

    private final S3Uploader s3Uploader;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public void createCartItem(String authId, MultipartFile imageFile, CartItemRequest dto) {
        CartItem cartItem = CartItem.createCartItem(authId, dto.getEa(), uploadImage(imageFile));
        Product product = productRepository.findById(dto.getProductId()).orElseThrow(); //예외처리 해야함
        cartItem.associate(product);

        for (OptionRequest optionRequest :dto.getOptionRequestList()){
            ProductCartOption productCartOption = new ProductCartOption();
            ProductOption productOption = productOptionRepository.findById(optionRequest.getOptionId()).orElseThrow();
            OptionDetail optionDetail = optionDetailRepository.findById(optionRequest.getDetailId()).orElseThrow();
            productCartOption.associate(productOption);
            productCartOption.associate(optionDetail);
            productCartOption.associate(cartItem);
        }
        cartItemRepository.save(cartItem);
    }

    @Override
    public void inialization() {
        Product product = null;
        ProductOption productOption;
        OptionDetail optionDetail;
        productRepository.save(product);
    }

    private String uploadImage(MultipartFile multipartFile) {
        if(multipartFile == null) {
            return null;
        }
        return s3Uploader.upload(multipartFile);
    }


}
