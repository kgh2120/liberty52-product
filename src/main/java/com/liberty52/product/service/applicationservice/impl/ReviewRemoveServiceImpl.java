package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.forbidden.InvalidRoleException;
import com.liberty52.product.global.exception.external.forbidden.NotYourReviewException;
import com.liberty52.product.global.exception.external.forbidden.InvalidRoleException;
import com.liberty52.product.global.exception.external.notfound.ReplyNotFoundByIdException;
import com.liberty52.product.global.exception.external.notfound.ReviewNotFoundByIdException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.ReviewRemoveService;
import com.liberty52.product.service.entity.Reply;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.event.internal.ImageRemovedEvent;
import com.liberty52.product.service.event.internal.dto.ImageRemovedEventDto;
import com.liberty52.product.service.repository.ReplyRepository;
import com.liberty52.product.service.repository.ReviewQueryDslRepository;
import com.liberty52.product.service.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewRemoveServiceImpl implements ReviewRemoveService {

    private final ReviewRepository reviewRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ReviewQueryDslRepository reviewQueryDslRepository;
    private final ReplyRepository replyRepository;

    @Override
    public void removeReview(String reviewerId, String reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundByIdException(reviewId));
        if(!reviewerId.equals(review.getCustomProduct().getOrders().getAuthId())){
            throw new NotYourReviewException(reviewerId);
        }
        this.reviewRepository.delete(review);
        review.getReviewImages().forEach(ri -> eventPublisher.publishEvent(new ImageRemovedEvent(this, new ImageRemovedEventDto(ri.getUrl()))));
    }

    @Override
    public void removeAllReview(String reviewerId) {
        List<Review> reviews = reviewQueryDslRepository.retrieveReviewByWriterId(reviewerId);
        for (Review review : reviews) {
            review.getReviewImages().forEach(ri -> eventPublisher.publishEvent(new ImageRemovedEvent(this, new ImageRemovedEventDto(ri.getUrl()))));
        }
        reviewRepository.deleteAll(reviews);
    }

    @Override
    public void removeCustomerReviewByAdmin(String role, String reviewId) {
        Validator.isAdmin(role);
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundByIdException(reviewId));
        this.reviewRepository.delete(review);
        review.getReviewImages().forEach(ri -> eventPublisher.publishEvent(new ImageRemovedEvent(this, new ImageRemovedEventDto(ri.getUrl()))));
    }

    @Override
    public void removeReplyByAdmin(String adminId, String role, String replyId) {
        Validator.isAdmin(role);
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundByIdException(replyId));
        reply.removeReview();
        replyRepository.delete(reply);
    }
}
