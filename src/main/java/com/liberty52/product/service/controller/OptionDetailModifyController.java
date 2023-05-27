package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.OptionDetailModifyService;
import com.liberty52.product.service.controller.dto.OptionDetailModifyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class OptionDetailModifyController {

    private final OptionDetailModifyService optionDetailModifyService;

    @PutMapping("/admin/optionDetail/{optionDetailId}")
    @ResponseStatus(HttpStatus.OK)
    public void modifyOptionDetailByAdmin(@RequestHeader("LB-Role") String role, @PathVariable String optionDetailId, @Validated @RequestBody OptionDetailModifyRequestDto dto) {
        optionDetailModifyService.modifyOptionDetailByAdmin(role, optionDetailId, dto);
    }

    @PutMapping("/admin/optionDetailOnSale/{optionDetailId}")
    @ResponseStatus(HttpStatus.OK)
    public void modifyOptionDetailOnSailStateByAdmin(@RequestHeader("LB-Role") String role, @PathVariable String optionDetailId) {
        optionDetailModifyService.modifyOptionDetailOnSailStateByAdmin(role, optionDetailId);
    }
}
