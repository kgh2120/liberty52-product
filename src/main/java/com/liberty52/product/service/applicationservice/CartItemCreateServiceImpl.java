package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.adapter.S3Uploader;
import com.liberty52.product.global.exception.external.NotMatchOptionException;
import com.liberty52.product.global.exception.external.OptionDetailNotFoundException;
import com.liberty52.product.global.exception.external.OptionNotFoundException;
import com.liberty52.product.global.exception.external.ProductNotFoundException;
import com.liberty52.product.service.controller.dto.CartItemRequest;
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
    private final ProductCartOptionRepository productCartOptionRepository;

    @Override
    public void createCartItem(String authId, MultipartFile imageFile, CartItemRequest dto) {
        CartItem cartItem = CartItem.createCartItem(authId, dto.getEa(), uploadImage(imageFile));
        Product product = productRepository.findById(dto.getProductId()).orElseThrow(() -> new ProductNotFoundException(dto.getProductId())); //예외처리 해야함
        cartItem.associate(product);
        cartItemRepository.save(cartItem);
        for (CartItemRequest.OptionRequest optionRequest :dto.getOptionRequestList()){
            ProductCartOption productCartOption = new ProductCartOption();
            ProductOption productOption = productOptionRepository.findById(optionRequest.getOptionId()).orElseThrow(() -> new OptionNotFoundException(dto.getProductId()));
            OptionDetail optionDetail = optionDetailRepository.findById(optionRequest.getDetailId()).orElseThrow(() -> new OptionDetailNotFoundException(dto.getProductId()));
            if(!product.getId().equals(productOption.getProduct().getId()) || !productOption.getId().equals(optionDetail.getProductOption().getId())) {
                throw new NotMatchOptionException();
            }
            productCartOption.associate(productOption);
            productCartOption.associate(optionDetail);
            productCartOption.associate(cartItem);
            productCartOptionRepository.save(productCartOption);
        }
        cartItemRepository.save(cartItem);
    }
//데모용 나중에 지워야 한다
    @Override
    public void init() {
        Product product = new Product();
        product.init("L1", "Liberty52", ProductState.ON_SAIL, (long)100000);
        productRepository.save(product);

        ProductOption productOption1 = new ProductOption();
        productOption1.init("a", "거치 방식 선택", true);
        productOption1.associate(product);
        productOptionRepository.save(productOption1);

        OptionDetail optionDetail1 = new OptionDetail();
        optionDetail1.init("a1", "이젤 거치형", 0);
        optionDetail1.associate(productOption1);
        optionDetailRepository.save(optionDetail1);

        OptionDetail optionDetail2 = new OptionDetail();
        optionDetail2.init("a2", "벽걸이형", 0);
        optionDetail2.associate(productOption1);
        optionDetailRepository.save(optionDetail2);

        ProductOption productOption2 = new ProductOption();
        productOption2.init("b", "기본소재 선택", true);
        productOption2.associate(product);
        productOptionRepository.save(productOption2);

        OptionDetail optionDetail3 = new OptionDetail();
        optionDetail3.init("b1", "1mm 두께 승화전사 인쇄용 알루미늄시트", 0);
        optionDetail3.associate(productOption2);
        optionDetailRepository.save(optionDetail3);

        ProductOption productOption3 = new ProductOption();
        productOption3.init("c", "기본소재 옵션", true);
        productOption3.associate(product);
        productOptionRepository.save(productOption3);

        OptionDetail optionDetail4 = new OptionDetail();
        optionDetail4.init("c1", "유광실버", 0);
        optionDetail4.associate(productOption3);
        optionDetailRepository.save(optionDetail4);

        OptionDetail optionDetail5 = new OptionDetail();
        optionDetail5.init("c2", "무광실버", 0);
        optionDetail5.associate(productOption3);
        optionDetailRepository.save(optionDetail5);

        OptionDetail optionDetail6 = new OptionDetail();
        optionDetail6.init("c3", "유광백색", 0);
        optionDetail6.associate(productOption3);
        optionDetailRepository.save(optionDetail6);

        OptionDetail optionDetail7 = new OptionDetail();
        optionDetail7.init("c4", "무광백색", 0);
        optionDetail7.associate(productOption3);
        optionDetailRepository.save(optionDetail7);

        productRepository.save(product);
        productOptionRepository.save(productOption1);
        productOptionRepository.save(productOption2);
        productOptionRepository.save(productOption3);

    }

    private String uploadImage(MultipartFile multipartFile) {
        if(multipartFile == null) {
            return "test"; //수정해야 할 부분
        }
        return s3Uploader.upload(multipartFile);
    }


}
