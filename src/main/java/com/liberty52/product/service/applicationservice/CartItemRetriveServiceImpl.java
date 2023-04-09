package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.NotFoundCartItemException;
import com.liberty52.product.service.controller.dto.CartItemResponse;
import com.liberty52.product.service.controller.dto.CartOptionResponse;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemRetriveServiceImpl implements CartItemRetriveService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductCartOptionRepository productCartOptionRepository;

    @Override
    public List<CartItemResponse> retriveCartItem(String authId) {
        List<CartItemResponse> cartItemResponseList = new ArrayList<CartItemResponse>();


        List<CartItem> cartItemList = cartItemRepository.findByAuthId(authId);
        if (cartItemList.size() == 0){
            throw new NotFoundCartItemException();
        }

        for(CartItem cartItem:cartItemList){
            Product product = cartItem.getProduct();
            CartItemResponse cartItemResponse = CartItemResponse.of(product.getId(), product.getName(), product.getPrice(), cartItem.getEa(), cartItem.getImageUrl(), getCartOptionList(cartItem.getOptions()));
            cartItemResponseList.add(cartItemResponse);
        }
        return cartItemResponseList;
    }

    private List<CartOptionResponse> getCartOptionList(List<ProductCartOption> options) {
        if (options.size() == 0) {
            return null;
        }
        List<CartOptionResponse> cartOptionResponseList = new ArrayList<CartOptionResponse>();

        for (ProductCartOption productCartOption : options) {
            ProductOption productOption = productCartOption.getProductOption();
            OptionDetail optionDetail = productCartOption.getOptionDetail();
            CartOptionResponse cartOptionResponse = CartOptionResponse.of(productOption.getId(), productOption.getName(), optionDetail.getId(), optionDetail.getName(), optionDetail.getPrice());
            cartOptionResponseList.add(cartOptionResponse);
        }
        return cartOptionResponseList;
    }
}
