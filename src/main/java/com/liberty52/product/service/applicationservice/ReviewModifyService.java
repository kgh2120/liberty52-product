package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.ReviewImagesRemoveRequestDto;
import com.liberty52.product.service.controller.dto.ReviewModifyRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewModifyService {
    void modifyReviewRatingContent(String reviewerId, String reviewId, ReviewModifyRequestDto dto);

    <T extends MultipartFile> void addReviewImages(String reviewerId, String reviewId, List<T> images);

    void removeReviewImages(String reviewerId, String reviewId, ReviewImagesRemoveRequestDto dto);

    <T extends MultipartFile> void modifyReview(String reviewerId, String reviewId, ReviewModifyRequestDto dto, List<T> images);
}
