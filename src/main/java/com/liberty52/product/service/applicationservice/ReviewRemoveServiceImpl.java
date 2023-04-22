package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.NoYourReviewException;
import com.liberty52.product.global.exception.external.ReviewNotFoundException;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewRemoveServiceImpl implements ReviewRemoveService {

    private final ReviewRepository reviewRepository;

    @Override
    public void removeReview(String reviewerId, String reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException());
        if(!reviewerId.equals(review.getOrder().getAuthId())){
            throw new NoYourReviewException();
        }

        this.reviewRepository.delete(review);
    }
}
