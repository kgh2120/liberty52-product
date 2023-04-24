package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.*;
import org.springframework.web.multipart.MultipartFile;

public interface MonoItemOrderService {

    @Deprecated
    MonoItemOrderResponseDto save(String authId, MultipartFile imageFile, MonoItemOrderRequestDto dto);

    PreregisterOrderResponseDto preregisterCardPaymentOrders(String authId, PreregisterOrderRequestDto dto, MultipartFile imageFile);

    ConfirmCardPaymentResponseDto confirmFinalApprovalOfCardPayment(String authId, String orderId);
}
