package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.MonoItemOrderRequestDto;
import com.liberty52.product.service.controller.dto.MonoItemOrderResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface MonoItemOrderService {
    MonoItemOrderResponseDto save(String authId, MultipartFile imageFile, MonoItemOrderRequestDto dto);
}
