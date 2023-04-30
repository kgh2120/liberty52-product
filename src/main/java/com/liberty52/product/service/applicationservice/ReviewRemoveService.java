package com.liberty52.product.service.applicationservice;

public interface ReviewRemoveService {
    void removeReview(String reviewerId, String reviewId);

    void removeAllReview(String reviewerId);
}
