package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.service.applicationservice.CartItemSchedulerService;
import com.liberty52.product.service.entity.Cart;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.event.internal.ImageRemovedEvent;
import com.liberty52.product.service.event.internal.dto.ImageRemovedEventDto;
import com.liberty52.product.service.repository.CartRepository;
import com.liberty52.product.service.repository.CustomProductOptionRepository;
import com.liberty52.product.service.repository.CustomProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemSchedulerServiceImpl implements CartItemSchedulerService {

    private final CartRepository cartRepository;
    private final CustomProductRepository customProductRepository;
    private final CustomProductOptionRepository customProductOptionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteNonMemberCart() {
        List<Cart> carts = cartRepository.findByExpiryDateLessThan(LocalDate.now());
        if(carts.size() > 0 ){
            for(Cart cart : carts){
                if(cart.getCustomProducts().size() > 0) {
                    deleteCustomProduct(cart);
                }
                cartRepository.delete(cart);
            }
        }
    }

    private void deleteCustomProduct(Cart cart) {
        List<String> urls = new ArrayList<>();
        List<CustomProduct> customProducts = cart.getCustomProducts();
        for(CustomProduct customProduct : customProducts){
            if(customProduct.getOptions().size() > 0) {
                urls.add(customProduct.getUserCustomPictureUrl());
                customProductOptionRepository.deleteAll(customProduct.getOptions());
            }
            customProductRepository.delete(customProduct);
        }
        urls.forEach(url -> eventPublisher.publishEvent(new ImageRemovedEvent(this, new ImageRemovedEventDto(url))));
    }

}
