package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.adapter.S3Uploader;
import com.liberty52.product.global.exception.external.BadRequestException;
import com.liberty52.product.global.exception.external.NotYourResourceException;
import com.liberty52.product.global.exception.external.ResourceNotFoundException;
import com.liberty52.product.service.controller.dto.ReviewImagesRemoveRequestDto;
import com.liberty52.product.service.controller.dto.ReviewModifyRequestDto;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.entity.ReviewImage;
import com.liberty52.product.service.event.internal.ImageRemovedEvent;
import com.liberty52.product.service.event.internal.dto.ImageRemovedEventDto;
import com.liberty52.product.service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewModifyServiceImpl implements ReviewModifyService {
    private static final String RESOURCE_NAME_REVIEW = "Review";
    private static final String PARAM_NAME_ID = "ID";
    private final ApplicationEventPublisher eventPublisher;
    private final ReviewRepository reviewRepository;
    private final S3Uploader s3Uploader;

    @Override
    public void modifyRatingContent(String reviewerId, String reviewId, ReviewModifyRequestDto dto) {
        Review review = validAndGetReview(reviewerId, reviewId);
        review.modify(dto.getRating(), dto.getContent());
    }

    @Override
    public <T extends MultipartFile> void addImages(String reviewerId, String reviewId, List<T> images) {
        if(images.size() > Review.IMAGES_MAX_COUNT || images.isEmpty())
            throw new BadRequestException(1 + " <= Size of images <= " + Review.IMAGES_MAX_COUNT);
        Review review = validAndGetReview(reviewerId, reviewId);
        addImagesInReview(review, images);
    }

    @Override
    public void removeImages(String reviewerId, String reviewId, ReviewImagesRemoveRequestDto dto) {
        Review review = validAndGetReview(reviewerId, reviewId);
        List<ReviewImage> reviewImages = review.getReviewImages().stream().filter(ri -> dto.getUrls().contains(ri.getUrl())).toList();
//        review.removeImagesByUrl(new HashSet<>(dto.getUrls()));
        reviewImages.forEach(review::removeImage);
        reviewImages.forEach(ri -> eventPublisher.publishEvent(new ImageRemovedEvent(this, new ImageRemovedEventDto(ri.getUrl()))));
    }

    @Override
    public <T extends MultipartFile> void modifyReview(String reviewerId, String reviewId, ReviewModifyRequestDto dto, List<T> images) {
        Review review = validAndGetReview(reviewerId, reviewId);
        review.modify(dto.getRating(), dto.getContent());
        List<String> urls = review.getReviewImages().stream().map(ReviewImage::getUrl).toList();
        review.clearImages();
        addImagesInReview(review, images);
        urls.forEach(url -> eventPublisher.publishEvent(new ImageRemovedEvent(this, new ImageRemovedEventDto(url))));
    }

    private <T extends MultipartFile> void addImagesInReview(Review review, List<T> images) {
        for (MultipartFile image : images) {
            if(!review.isImageAddable()) break;
            String url = s3Uploader.upload(image);
            ReviewImage.create(review, url);
        }
    }

    private Review validAndGetReview(String reviewerId, String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME_REVIEW, PARAM_NAME_ID, reviewId));
        if(!reviewerId.equals(review.getOrder().getAuthId()))
            throw new NotYourResourceException(RESOURCE_NAME_REVIEW, reviewerId);
        return review;
    }
}
