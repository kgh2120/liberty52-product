package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.DeliveryOptionModifyService;
import com.liberty52.product.service.controller.dto.DeliveryOptionDto;
import com.liberty52.product.service.entity.DeliveryOption;
import com.liberty52.product.service.repository.DeliveryOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryOptionModifyServiceImpl implements DeliveryOptionModifyService {

    private final DeliveryOptionRepository deliveryOptionRepository;
    
    @Override
    @Transactional
    public DeliveryOptionDto updateDefaultDeliveryFeeByAdmin(String role, int fee) {
        Validator.isAdmin(role);

        DeliveryOption deliveryOption = deliveryOptionRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("DELIVERY_OPTION", "id", "1"));

        deliveryOption.updateFee(fee);

        return DeliveryOptionDto.fromEntity(deliveryOption);
    }
}
