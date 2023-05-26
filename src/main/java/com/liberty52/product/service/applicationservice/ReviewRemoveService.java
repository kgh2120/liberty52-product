package com.liberty52.product.service.applicationservice;

public interface ReviewRemoveService {
    void removeReview(String reviewerId, String reviewId);

    void removeAllReview(String reviewerId);

    void removeCustomerReviewByAdmin(String role, String reviewId);

    void removeReplyByAdmin(String adminId, String role, String replyId);
}
