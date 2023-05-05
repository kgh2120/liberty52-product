package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.ReplyCreateRequestDto;

public interface ReplyCreateService {
    void createReply(String adminId, ReplyCreateRequestDto dto, String reviewId, String role);

}
