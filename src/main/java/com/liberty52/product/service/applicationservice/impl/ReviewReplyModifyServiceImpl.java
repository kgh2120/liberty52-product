package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.badrequest.BadRequestException;
import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.ReviewReplyModifyService;
import com.liberty52.product.service.controller.dto.ReplyModifyRequestDto;
import com.liberty52.product.service.entity.Reply;
import com.liberty52.product.service.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewReplyModifyServiceImpl implements ReviewReplyModifyService {
    private final ReplyRepository replyRepository;

    @Override
    public void modifyReviewReplyByAdmin(String adminId, String role, ReplyModifyRequestDto dto, String reviewId, String replyId) {
      Validator.isAdmin(role);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResourceNotFoundException("Reply", "id", replyId));
        if(!reply.getReview().getId().equals(reviewId))
            throw new BadRequestException("Review와 Reply가 매칭되지 않습니다.");
        reply.modify(dto.getContent());
    }
}
