package com.liberty52.product.service.applicationservice.impl;

import static com.liberty52.product.global.contants.RoleConstants.ADMIN;

import com.liberty52.product.global.exception.external.forbidden.InvalidRoleException;
import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.service.applicationservice.ReplyCreateService;
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
public class ReplyCreateServiceImpl implements
        ReplyCreateService {

    private final ReviewRepository reviewRepository;

    @Override
    public void createReply(String reviewerId, ReplyCreateRequestDto dto, String reviewId,
            String role) {
        if(!ADMIN.equals(role))
            throw new InvalidRoleException(role);



        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "ID", reviewId));
        Reply reply = Reply.create(dto.getContent(), reviewerId);
        reply.associate(review);
    }
}
