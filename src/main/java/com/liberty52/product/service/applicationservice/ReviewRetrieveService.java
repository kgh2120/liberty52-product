package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.ReviewRetrieveResponse;
import org.springframework.data.domain.Pageable;

public interface ReviewRetrieveService {

    ReviewRetrieveResponse retrieveReviews(String productId, String authorId, Pageable pageable, boolean isPhotoFilter);

}
