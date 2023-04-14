package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.adapter.S3Uploader;
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
    private final CustomProductOptionRepository productCartOptionRepository;

    @Override
    public void createCartItem(String authId, MultipartFile imageFile, CartItemRequest dto) {
        Cart cart = cartRepository.findByAuthId(authId).orElseGet(() -> createCart(authId));

        CustomProduct cartItem = CustomProduct.createCartItem(authId, dto.getQuantity(), uploadImage(imageFile));
        Product product = productRepository.findByName(dto.getProductName()).orElseThrow(() -> new ProductNotFoundException(dto.getProductName())); //예외처리 해야함
        cartItem.associateWithProduct(product);
        cartItemRepository.save(cartItem);
        for (String optionDetailName :dto.getOptions()){
            CustomProductOption productCartOption = CustomProductOption.create();
            OptionDetail optionDetail = optionDetailRepository.findByName(optionDetailName).orElseThrow(() -> new OptionDetailNotFoundException(optionDetailName));

            productCartOption.associate(optionDetail);
            productCartOption.associate(cartItem);
            productCartOptionRepository.save(productCartOption);
        }
        cartItem.associateWithCart(cart);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    private Cart createCart(String authId) {
        Cart cart = Cart.create(authId);
        cartRepository.save(cart);
        return cart;
    }

    //데모용 나중에 지워야 한다
    @Override
    public void init() {
        Product product = Product.create("Liberty52",ProductState.ON_SAIL, (long)1000000);
        productRepository.save(product);

        ProductOption productOption1 = ProductOption.create("거치 방식 선택", true);
        productOption1.associate(product);
        productOptionRepository.save(productOption1);

        OptionDetail optionDetail1 = OptionDetail.create("이젤 거치형", 500000);
        optionDetail1.associate(productOption1);
        optionDetailRepository.save(optionDetail1);

        OptionDetail optionDetail2 = OptionDetail.create("벽걸이형", 300000);
        optionDetail2.associate(productOption1);
        optionDetailRepository.save(optionDetail2);

        ProductOption productOption2 = ProductOption.create("기본소재 선택", true);
        productOption2.associate(product);
        productOptionRepository.save(productOption2);

        OptionDetail optionDetail3 = OptionDetail.create("1mm 두께 승화전사 인쇄용 알루미늄시트", 0);
        optionDetail3.associate(productOption2);
        optionDetailRepository.save(optionDetail3);

        ProductOption productOption3 = ProductOption.create("기본소재 옵션", true);
        productOption3.associate(product);
        productOptionRepository.save(productOption3);

        OptionDetail optionDetail4 = OptionDetail.create("유광실버", 600000);
        optionDetail4.associate(productOption3);
        optionDetailRepository.save(optionDetail4);

        OptionDetail optionDetail5 = OptionDetail.create("무광실버", 400000);
        optionDetail5.associate(productOption3);
        optionDetailRepository.save(optionDetail5);

        OptionDetail optionDetail6 = OptionDetail.create("유광백색", 300000);
        optionDetail6.associate(productOption3);
        optionDetailRepository.save(optionDetail6);

        OptionDetail optionDetail7 = OptionDetail.create("무광백색", 500000);
        optionDetail7.associate(productOption3);
        optionDetailRepository.save(optionDetail7);

        productRepository.save(product);
        productOptionRepository.save(productOption1);
        productOptionRepository.save(productOption2);
        productOptionRepository.save(productOption3);

    }

    private String uploadImage(MultipartFile multipartFile) {
        if(multipartFile == null) {
            return ""; //수정해야 할 부분
        }
        return s3Uploader.upload(multipartFile);
    }


}
