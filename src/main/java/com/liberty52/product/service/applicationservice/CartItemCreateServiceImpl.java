package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.adapter.S3Uploader;
import com.liberty52.product.global.exception.external.OptionDetailNotFoundException;
import com.liberty52.product.global.exception.external.ProductNotFoundException;
import com.liberty52.product.service.controller.dto.CartItemRequest;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemCreateServiceImpl implements CartItemCreateService{

    private final S3Uploader s3Uploader;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CustomProductOptionRepository customProductOptionRepository;


    @Override
    public void createCartItem(String authId, MultipartFile imageFile, CartItemRequest dto) {
        Cart cart = cartRepository.findByAuthId(authId).orElseGet(() -> createCart(authId));

        CustomProduct customProduct = CustomProduct.createCartItem(authId, dto.getQuantity(), uploadImage(imageFile));
        customProduct.associateWithCart(cart);

        Product product = productRepository.findByName(dto.getProductName()).orElseThrow(() -> new ProductNotFoundException(dto.getProductName())); //예외처리 해야함
        customProduct.associateWithProduct(product);
        cartItemRepository.save(customProduct);

        for (String optionDetailName :dto.getOptions()){
            CustomProductOption customProductOption = CustomProductOption.create();
            OptionDetail optionDetail = optionDetailRepository.findByName(optionDetailName).orElseThrow(() -> new OptionDetailNotFoundException(optionDetailName));

            customProductOption.associate(optionDetail);
            customProductOption.associate(customProduct);
            customProductOptionRepository.save(customProductOption);
        }


    }

    private Cart createCart(String authId) {
        Cart cart = Cart.create(authId);
        cart = cartRepository.save(cart);
        return cart;
    }


    private String uploadImage(MultipartFile multipartFile) {
        if(multipartFile == null) {
            return ""; //수정해야 할 부분
        }
        return s3Uploader.upload(multipartFile);
    }


}
