package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.NotYourResourceException;
import com.liberty52.product.global.exception.external.ResourceNotFoundException;
import com.liberty52.product.global.exception.external.UnRemovableResourceException;
import com.liberty52.product.service.controller.dto.CartItemListRemoveRequestDto;
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
    private static final String RESOURCE_NAME_CART_ITEM = "CartItem(CustomProduct in Cart)";
    private static final String PARAM_NAME_ID = "ID";
    private static final String RESOURCE_NAME_ORDER_ITEM = "OrderItem(CustomProduct in Order)";


    @Override
    public void removeCartItem(String authId, String cartItemId) {
        CustomProduct cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME_CART_ITEM, PARAM_NAME_ID, cartItemId));
        if(!authId.equals(cartItem.getAuthId()))
            throw new NotYourResourceException(RESOURCE_NAME_CART_ITEM, authId);
        if (cartItem.isInOrder())
            throw new UnRemovableResourceException(RESOURCE_NAME_ORDER_ITEM, cartItemId);
        productCartOptionRepository.deleteAll(cartItem.getOptions());
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void removeCartItemList(String authId, CartItemListRemoveRequestDto dto) {
        dto.getIds().stream()
                .map(id -> cartItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME_CART_ITEM, PARAM_NAME_ID, id)))
                .forEach(cp -> {
                    if(!authId.equals((cp.getAuthId())))
                        throw new NotYourResourceException(RESOURCE_NAME_CART_ITEM, authId);
                    if (cp.isInOrder())
                        throw new UnRemovableResourceException(RESOURCE_NAME_ORDER_ITEM, cp.getId());
                    productCartOptionRepository.deleteAll(cp.getOptions());
                    cartItemRepository.delete(cp);
                });
    }
}
