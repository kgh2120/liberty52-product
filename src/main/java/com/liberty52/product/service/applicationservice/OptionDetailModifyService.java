package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.OptionDetailModifyRequestDto;
import com.liberty52.product.service.controller.dto.OptionDetailOnSailModifyRequestDto;

public interface OptionDetailModifyService {
    void modifyOptionDetailByAdmin(String role, String optionDetailId, OptionDetailModifyRequestDto dto);

    void modifyOptionDetailOnSailStateByAdmin(String role, String optionDetailId, OptionDetailOnSailModifyRequestDto dto);
}
