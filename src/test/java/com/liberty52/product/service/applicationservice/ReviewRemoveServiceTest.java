package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.config.DBInitConfig;
import com.liberty52.product.global.exception.external.forbidden.NotYourReviewException;
import com.liberty52.product.global.exception.external.notfound.ReviewNotFoundByIdException;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@Transactional
public class ReviewRemoveServiceTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewRemoveService reviewRemoveService;

    private Review review;

    String reviewerId;
    @BeforeEach
    void beforeEach() {
        review = DBInitConfig.DBInitService.getReview();
    }

    @Test
    void 리뷰삭제() {
        Review reviewBefore = reviewRepository.findById(review.getId()).orElse(null);
        Assertions.assertNotNull(reviewBefore);

        Assertions.assertThrows(ReviewNotFoundByIdException.class, () -> reviewRemoveService.removeReview(reviewerId, randomString()));
        Assertions.assertThrows(NotYourReviewException.class, () -> reviewRemoveService.removeReview(randomString(), review.getId()));

        reviewRemoveService.removeReview(review.getOrder().getAuthId(), review.getId());
        Review reviewAfter = reviewRepository.findById(review.getId()).orElse(null);
        Assertions.assertNull(reviewAfter);
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }
}
