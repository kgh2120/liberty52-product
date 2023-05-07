package com.liberty52.product.service.repository;

import com.liberty52.product.service.controller.dto.AdminReviewRetrieveResponse;
import com.liberty52.product.service.controller.dto.ReviewRetrieveResponse;
import com.liberty52.product.service.entity.Review;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ReviewQueryDslRepository {

    ReviewRetrieveResponse retrieveReview(String productId, String authorId, Pageable pageable,
            boolean isPhotoFilter);

    List<Review> retrieveReviewByWriterId(String writerId);

    AdminReviewRetrieveResponse retrieveAllReviews(Pageable pageable);
}
