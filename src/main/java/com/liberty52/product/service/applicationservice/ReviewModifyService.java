package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.ReviewImagesRemoveRequestDto;
import com.liberty52.product.service.controller.dto.ReviewModifyRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewModifyService {
    void modifyReview(String reviewerId, String reviewId, ReviewModifyRequestDto dto);

    <T extends MultipartFile> void addImages(String reviewerId, String reviewId, List<T> images);

    void removeImages(String reviewerId, String reviewId, ReviewImagesRemoveRequestDto dto);
}
