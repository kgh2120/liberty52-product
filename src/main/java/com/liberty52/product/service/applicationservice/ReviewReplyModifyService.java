package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.ReplyModifyRequestDto;

public interface ReviewReplyModifyService {
    void modifyReviewReplyByAdmin(String adminId, String role, ReplyModifyRequestDto dto, String reviewId, String replyId);
}
