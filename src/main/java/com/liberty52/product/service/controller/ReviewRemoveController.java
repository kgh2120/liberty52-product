package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.ReviewRemoveService;
import com.liberty52.product.service.controller.dto.ReplyCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewRemoveController {
    private final ReviewRemoveService reviewItemRemoveService;

    @DeleteMapping("/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeReview(@RequestHeader(HttpHeaders.AUTHORIZATION) String reviewerId, @PathVariable String reviewId) {
        reviewItemRemoveService.removeReview(reviewerId, reviewId);
    }

    @DeleteMapping("/customerReviews/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeCustomerReview(@RequestHeader(HttpHeaders.AUTHORIZATION) String adminId, @RequestHeader("LB-Role") String role, @PathVariable String reviewId) {

        reviewItemRemoveService.removeCustomerReview(role, reviewId);
    }

    @DeleteMapping("/reviews/replies/{replyId}")
    @ResponseStatus(HttpStatus.OK)
    public void replyRemove(@RequestHeader(HttpHeaders.AUTHORIZATION) String reviewerId, @RequestHeader("LB-Role") String role, @PathVariable String replyId) {
        reviewItemRemoveService.removeReply(reviewerId, role ,replyId);
    }
}
