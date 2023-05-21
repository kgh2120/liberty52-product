package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.OptionDetailRemoveRequestDto;

public interface OptionDetailRemoveService {
    void removeOptionDetail(String role, String optionDetailId, OptionDetailRemoveRequestDto dto);
}
