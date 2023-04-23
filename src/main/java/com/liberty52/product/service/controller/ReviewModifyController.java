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
    public void reviewModify(@RequestHeader(HttpHeaders.AUTHORIZATION) String reviewerId,
                             @PathVariable String reviewId,
                             @Validated @RequestPart ReviewModifyRequestDto dto,
                             @RequestPart List<MultipartFile> images) {
        reviewModifyService.modifyReview(reviewerId, reviewId, dto, images);
    }

    @PatchMapping("/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reviewRatingContentModify(@RequestHeader(HttpHeaders.AUTHORIZATION) String reviewerId,
                                          @PathVariable String reviewId,
                                          @Validated @RequestBody ReviewModifyRequestDto dto) {
        reviewModifyService.modifyRatingContent(reviewerId, reviewId, dto);
    }

    @PostMapping("/reviews/{reviewId}/images") // 이미지 추가
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reviewImagesAdd(@RequestHeader(HttpHeaders.AUTHORIZATION) String reviewerId,
                                @PathVariable String reviewId,
                                @RequestPart List<MultipartFile> images) {
        reviewModifyService.addImages(reviewerId, reviewId, images);
    }

    @DeleteMapping("/reviews/{reviewId}/images")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reviewImagesRemove(@RequestHeader(HttpHeaders.AUTHORIZATION) String reviewerId,
                                   @PathVariable String reviewId,
                                   @Validated @RequestBody ReviewImagesRemoveRequestDto dto) {
        reviewModifyService.removeImages(reviewerId, reviewId, dto);
    }
}
