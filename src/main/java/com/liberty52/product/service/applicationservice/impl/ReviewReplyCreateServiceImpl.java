package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.ReviewReplyCreateService;
import com.liberty52.product.service.controller.dto.ReplyCreateRequestDto;
import com.liberty52.product.service.entity.Reply;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewReplyCreateServiceImpl implements
    ReviewReplyCreateService {

    private final ReviewRepository reviewRepository;

    @Override
    public void createReviewReplyByAdmin(String reviewerId, ReplyCreateRequestDto dto, String reviewId,
            String role) {
      Validator.isAdmin(role);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "ID", reviewId));
        Reply reply = Reply.create(dto.getContent(), reviewerId);
        reply.associate(review);
    }
}
