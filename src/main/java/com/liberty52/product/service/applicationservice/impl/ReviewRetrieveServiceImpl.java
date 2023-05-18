package com.liberty52.product.service.applicationservice.impl;

import static com.liberty52.product.global.contants.RoleConstants.ADMIN;

import com.liberty52.product.global.adapter.cloud.AuthServiceClient;
import com.liberty52.product.global.exception.external.forbidden.InvalidRoleException;
import com.liberty52.product.service.applicationservice.ReviewRetrieveService;
import com.liberty52.product.service.controller.dto.AdminReviewRetrieveResponse;
import com.liberty52.product.service.controller.dto.ReviewRetrieveResponse;
import com.liberty52.product.service.repository.ReviewQueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class ReviewRetrieveServiceImpl implements
        ReviewRetrieveService {

    private final ReviewQueryDslRepository reviewQueryDslRepository;
    private final AuthServiceClient authServiceClient;



    @Override
    public ReviewRetrieveResponse retrieveReviews(String productId, String authorId,
            Pageable pageable,  boolean isPhotoFilter ) {
        ReviewRetrieveResponse response = reviewQueryDslRepository.retrieveReview(
                productId, authorId, pageable, isPhotoFilter);

        setAuthorDataFromAuthService(response);
        return response;
    }

    private void setAuthorDataFromAuthService(ReviewRetrieveResponse response) {
        response.setReviewAuthor(authServiceClient.retrieveAuthData(response.getAuthorIds()));
    }

    @Override
    public AdminReviewRetrieveResponse retrieveReviews(String role, Pageable pageable) {
        if(!ADMIN.equals(role))
            throw new InvalidRoleException(role);
        AdminReviewRetrieveResponse response = reviewQueryDslRepository.retrieveAllReviews(pageable);
        setAuthorDataFromAuthService(response);
        return response;
    }
    private void setAuthorDataFromAuthService(AdminReviewRetrieveResponse response) {
        response.setReviewAuthor(authServiceClient.retrieveAuthData(response.getAuthorIds()));
    }
}
