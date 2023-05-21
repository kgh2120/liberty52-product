package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.OptionDetailCreateService;
import com.liberty52.product.service.controller.dto.CreateOptionDetailRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OptionDetailCreateController {

    private final OptionDetailCreateService optionDetailCreateService;

    @PostMapping("/optionDetail/{optionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOptionDetail(@RequestHeader("LB-Role") String role,
                                   @Validated @RequestBody CreateOptionDetailRequestDto dto, @PathVariable String optionId) {
        optionDetailCreateService.createOptionDetail(role, dto, optionId);
    }
}
