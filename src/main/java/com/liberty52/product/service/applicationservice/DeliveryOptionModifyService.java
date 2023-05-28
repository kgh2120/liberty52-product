package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.DeliveryOptionDto;

public interface DeliveryOptionModifyService {
    DeliveryOptionDto updateDefaultDeliveryFeeByAdmin(String role, int fee);
}
