package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.adapter.cloud.AuthServiceClient;
import com.liberty52.product.service.controller.dto.ReviewRetrieveResponse;
import com.liberty52.product.service.repository.ReviewQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class ReviewRetrieveServiceImpl implements
        ReviewRetrieveService {

    private final ReviewQueryRepository reviewQueryRepository;
    private final AuthServiceClient authServiceClient;



    @Override
    public ReviewRetrieveResponse retrieveReviews(String productId, String authorId,
            Pageable pageable,  boolean isPhotoFilter ) {
        ReviewRetrieveResponse response = reviewQueryRepository.retrieveReview(
                productId, authorId, pageable, isPhotoFilter);

        setAuthorDataFromAuthService(response);
        return response;
    }

    private void setAuthorDataFromAuthService(ReviewRetrieveResponse response) {
        response.setReviewAuthor(authServiceClient.retrieveAuthData(response.getAuthorIds()));
    }
}
