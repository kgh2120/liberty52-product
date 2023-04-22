package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.config.DBInitConfig;
import com.liberty52.product.global.exception.external.NoYourReviewException;
import com.liberty52.product.global.exception.external.ReviewNotFoundException;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ReviewRemoveServiceTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewRemoveService reviewRemoveService;

    private Product product;
    private Orders order;
    private Review review;

    String reviewerId;
    @BeforeEach
    void beforeEach() {
        product = DBInitConfig.DBInitService.getProduct();
        order = DBInitConfig.DBInitService.getOrder();

        reviewerId = order.getAuthId();
        review = Review.create(4, "content");
        review.associate(order);
        review.associate(product);
        reviewRepository.save(review);

    }

    @Test
    void 리뷰삭제() {
        Review reviewBefore = reviewRepository.findById(review.getId()).orElse(null);
        Assertions.assertNotNull(reviewBefore);

        Assertions.assertThrows(ReviewNotFoundException.class, () -> reviewRemoveService.removeReview(reviewerId, "123"));
        Assertions.assertThrows(NoYourReviewException.class, () -> reviewRemoveService.removeReview("123", review.getId()));

        reviewRemoveService.removeReview(reviewerId, review.getId());
        Review reviewAfter = reviewRepository.findById(review.getId()).orElse(null);
        Assertions.assertNull(reviewAfter);

    }
}
