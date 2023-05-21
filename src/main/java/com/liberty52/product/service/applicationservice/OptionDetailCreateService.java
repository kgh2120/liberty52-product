package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.CreateOptionDetailRequestDto;

public interface OptionDetailCreateService {
    void createOptionDetail(String role, CreateOptionDetailRequestDto dto, String optionId);
}
