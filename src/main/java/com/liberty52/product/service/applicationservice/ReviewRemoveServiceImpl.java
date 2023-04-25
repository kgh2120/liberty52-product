package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.NotYourReviewException;
import com.liberty52.product.global.exception.external.ReviewNotFoundException;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.event.internal.ImageRemovedEvent;
import com.liberty52.product.service.event.internal.dto.ImageRemovedEventDto;
import com.liberty52.product.service.repository.ReviewRepository;
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

    @Override
    public void removeReview(String reviewerId, String reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        if(!reviewerId.equals(review.getOrder().getAuthId())){
            throw new NotYourReviewException();
        }
        this.reviewRepository.delete(review);
        review.getReviewImages().forEach(ri -> eventPublisher.publishEvent(new ImageRemovedEvent(this, new ImageRemovedEventDto(ri.getUrl()))));
    }
}
