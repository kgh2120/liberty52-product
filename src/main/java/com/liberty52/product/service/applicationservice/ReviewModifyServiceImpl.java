package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.adapter.S3Uploader;
import com.liberty52.product.global.exception.external.BadRequestException;
import com.liberty52.product.global.exception.external.NotYourResourceException;
import com.liberty52.product.global.exception.external.ResourceNotFoundException;
import com.liberty52.product.service.controller.dto.ReviewImagesRemoveRequestDto;
import com.liberty52.product.service.controller.dto.ReviewModifyRequestDto;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.entity.ReviewImage;
import com.liberty52.product.service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewModifyServiceImpl implements ReviewModifyService {
    private static final String RESOURCE_NAME_REVIEW = "Review";
    private static final String PARAM_NAME_ID = "ID";

    private final ReviewRepository reviewRepository;
    private final S3Uploader s3Uploader;

    @Override
    public void modifyReview(String reviewerId, String reviewId, ReviewModifyRequestDto dto) {
        Review review = validAndGetReview(reviewerId, reviewId);
        review.modify(dto.getRating(), dto.getContent()); // already validated by jakarta.validation
    }

    @Override
    public <T extends MultipartFile> void addImages(String reviewerId, String reviewId, List<T> images) {
        if(images.size() > Review.IMAGES_MAX_COUNT || images.isEmpty())
            throw new BadRequestException(1 + " <= Size of images <= " + Review.IMAGES_MAX_COUNT);
        Review review = validAndGetReview(reviewerId, reviewId);
        for (MultipartFile image : images) {
            if(!review.isImageAddable()) break;
            String url = s3Uploader.upload(image);
            ReviewImage.create(review, url);
        }
    }

    @Override
    public void removeImages(String reviewerId, String reviewId, ReviewImagesRemoveRequestDto dto) {
        Review review = validAndGetReview(reviewerId, reviewId);
        review.removeImagesByUrl(new HashSet<>(dto.getUrls()));
    }

    private Review validAndGetReview(String reviewerId, String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME_REVIEW, PARAM_NAME_ID, reviewId));
        if(!reviewerId.equals(review.getOrder().getAuthId()))
            throw new NotYourResourceException(RESOURCE_NAME_REVIEW, reviewerId);
        return review;
    }
}
