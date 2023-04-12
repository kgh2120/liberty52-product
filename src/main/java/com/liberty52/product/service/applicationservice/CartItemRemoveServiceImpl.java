package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.ForbiddenException;
import com.liberty52.product.global.exception.external.ResourceNotFoundException;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.repository.CartItemRepository;
import com.liberty52.product.service.repository.CustomProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartItemRemoveServiceImpl implements CartItemRemoveService {
    private final CartItemRepository cartItemRepository;
    private final CustomProductOptionRepository productCartOptionRepository;
    private static final String RESOURCE_NAME = "CartItem";
    private static final String PARAM_NAME_ID = "ID";


    @Override
    public void removeCartItem(String authId, String cartItemId) {
        CustomProduct cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, PARAM_NAME_ID, cartItemId));
        if(!authId.equals(cartItem.getAuthId()))
            throw new ForbiddenException(RESOURCE_NAME, authId);
        productCartOptionRepository.deleteAll(cartItem.getOptions());
        cartItemRepository.delete(cartItem);
    }
}
