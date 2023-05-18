package com.liberty52.product.service.controller;


import com.liberty52.product.service.applicationservice.ReviewRetrieveService;
import com.liberty52.product.service.controller.dto.AdminReviewRetrieveResponse;
import com.liberty52.product.service.controller.dto.ReviewRetrieveResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReviewRetrieveController {

    private final ReviewRetrieveService reviewRetrieveService;

    @GetMapping("/reviews/products/{productId}")
    public ResponseEntity<ReviewRetrieveResponse> retrieveReview(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)String identifier,
            @PathVariable String productId, Pageable pageable,
            @RequestParam(required = false) boolean photoFilter
    ){
        ReviewRetrieveResponse response = reviewRetrieveService.retrieveReviews(
                productId, identifier, pageable, photoFilter);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reviews")
    public ResponseEntity<AdminReviewRetrieveResponse> retrieveReview(@RequestHeader("LB-Role") String role, Pageable pageable){
        AdminReviewRetrieveResponse response = reviewRetrieveService.retrieveReviews(role,pageable);
        return ResponseEntity.ok(response);
    }
}
