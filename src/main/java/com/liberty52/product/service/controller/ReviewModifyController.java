package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.ReviewModifyService;
import com.liberty52.product.service.controller.dto.ReviewImagesRemoveRequestDto;
import com.liberty52.product.service.controller.dto.ReviewModifyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewModifyController {
    private final ReviewModifyService reviewModifyService;

    @PutMapping("/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyReview(@RequestHeader(HttpHeaders.AUTHORIZATION) String reviewerId,
                             @PathVariable String reviewId,
                             @Validated @RequestPart ReviewModifyRequestDto dto,
                             @RequestPart(required = false) List<MultipartFile> images) {
        reviewModifyService.modifyReview(reviewerId, reviewId, dto, images);
    }

    @PatchMapping("/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyReviewRatingContent(@RequestHeader(HttpHeaders.AUTHORIZATION) String reviewerId,
                                          @PathVariable String reviewId,
                                          @Validated @RequestBody ReviewModifyRequestDto dto) {
        reviewModifyService.modifyReviewRatingContent(reviewerId, reviewId, dto);
    }

    @PostMapping("/reviews/{reviewId}/images") // 이미지 추가
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addReviewImages(@RequestHeader(HttpHeaders.AUTHORIZATION) String reviewerId,
                                @PathVariable String reviewId,
                                @RequestPart List<MultipartFile> images) {
        reviewModifyService.addReviewImages(reviewerId, reviewId, images);
    }

    @DeleteMapping("/reviews/{reviewId}/images")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeReviewImages(@RequestHeader(HttpHeaders.AUTHORIZATION) String reviewerId,
                                   @PathVariable String reviewId,
                                   @Validated @RequestBody ReviewImagesRemoveRequestDto dto) {
        reviewModifyService.removeReviewImages(reviewerId, reviewId, dto);
    }
}
