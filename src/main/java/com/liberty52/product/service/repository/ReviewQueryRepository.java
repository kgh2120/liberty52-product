package com.liberty52.product.service.repository;

import com.liberty52.product.service.controller.dto.ReviewRetrieveResponse;
import org.springframework.data.domain.Pageable;

public interface ReviewQueryRepository {

    ReviewRetrieveResponse retrieveReview(String productId, String authorId, Pageable pageable,
            boolean isPhotoFilter);

}
