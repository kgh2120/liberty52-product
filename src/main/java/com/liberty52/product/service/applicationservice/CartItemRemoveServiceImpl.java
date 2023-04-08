package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.ForbiddenException;
import com.liberty52.product.global.exception.external.ResourceNotFoundException;
import com.liberty52.product.service.entity.CartItem;
import com.liberty52.product.service.repository.CartItemRepository;
import com.liberty52.product.service.repository.ProductCartOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartItemRemoveServiceImpl implements CartItemRemoveService {
    private final CartItemRepository cartItemRepository;
    private final ProductCartOptionRepository productCartOptionRepository;
    private static final String RESOURCE_NAME = "CartItem";
    private static final String PARAM_NAME_ID = "ID";


    @Override
    public void removeCartItem(String authId, String cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, PARAM_NAME_ID, cartItemId));
        if(!authId.equals(cartItem.getAuthId()))
            throw new ForbiddenException(RESOURCE_NAME, authId);
        productCartOptionRepository.deleteAll(cartItem.getOptions());
        cartItemRepository.delete(cartItem);
    }
}
