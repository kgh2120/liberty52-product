package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.CartItemResponse;
import com.liberty52.product.service.controller.dto.CartOptionResponse;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemRetriveServiceImpl implements CartItemRetriveService {

    private final CartRepository cartRepository;

    @Override
    public List<CartItemResponse> retriveAuthCartItem(String authId) {
        List<CartItemResponse> cartItemResponseList = new ArrayList<CartItemResponse>();
        Cart cart = cartRepository.findByAuthId(authId).orElse(null);

        if (cart==null || cart.getCustomProducts().size() == 0){
            return cartItemResponseList;
        }
        return retriveCartItem(cartItemResponseList, cart);
    }

    @Override
    public List<CartItemResponse> retriveGuestCartItem(String guestId) {
        List<CartItemResponse> cartItemResponseList = new ArrayList<CartItemResponse>();
        Cart cart = cartRepository.findByAuthIdAndExpiryDateGreaterThanEqual(guestId, LocalDate.now()).orElse(null);

        if (cart==null || cart.getCustomProducts().size() == 0){
            return cartItemResponseList;
        }
        return retriveCartItem(cartItemResponseList, cart);
    }

    private List<CartItemResponse> retriveCartItem(List<CartItemResponse> cartItemResponseList, Cart cart) {
        List<CustomProduct> cartItemList = cart.getCustomProducts();

        for(CustomProduct cartItem:cartItemList){
            Product product = cartItem.getProduct();
            CartItemResponse cartItemResponse = CartItemResponse.of(cartItem.getId(), product.getName(), cartItem.getUserCustomPictureUrl(), product.getPrice(), cartItem.getQuantity(), getCartOptionList(cartItem.getOptions()));
            cartItemResponseList.add(cartItemResponse);
        }
        return cartItemResponseList;
    }

    private List<CartOptionResponse> getCartOptionList(List<CustomProductOption> options) {
        if (options.size() == 0) {
            return null;
        }
        List<CartOptionResponse> cartOptionResponseList = new ArrayList<CartOptionResponse>();

        for (CustomProductOption productCartOption : options) {
            OptionDetail optionDetail = productCartOption.getOptionDetail();
            CartOptionResponse cartOptionResponse = CartOptionResponse.of(optionDetail.getProductOption().getName(), optionDetail.getName(), optionDetail.getPrice(), optionDetail.getProductOption().isRequire());
            cartOptionResponseList.add(cartOptionResponse);
        }
        return cartOptionResponseList;
    }
}
