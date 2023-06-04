package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.adapter.s3.S3UploaderApi;
import com.liberty52.product.global.exception.external.badrequest.BadRequestException;
import com.liberty52.product.global.exception.external.badrequest.ReviewAlreadyExistByCustomProductException;
import com.liberty52.product.global.exception.external.badrequest.ReviewCannotWriteByOrderStatusIsNotCompleteException;
import com.liberty52.product.global.exception.external.badrequest.ReviewCannotWriteInCartException;
import com.liberty52.product.global.exception.external.notfound.CustomProductNotFoundByIdException;
import com.liberty52.product.service.applicationservice.ReviewCreateService;
import com.liberty52.product.service.controller.dto.ReviewCreateRequestDto;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.entity.ReviewImage;
import com.liberty52.product.service.repository.CustomProductRepository;
import com.liberty52.product.service.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewCreateServiceImpl implements ReviewCreateService {

  private final ReviewRepository reviewRepository;
  private final CustomProductRepository customProductRepository;
  private final S3UploaderApi s3Uploader;

  @Override
  public void createReview(String reviewerId, ReviewCreateRequestDto dto, List<MultipartFile> images) {
    CustomProduct customProduct = customProductRepository.findById(dto.getCustomProductId())
        .orElseThrow(() -> new CustomProductNotFoundByIdException(dto.getCustomProductId()));

    if (customProduct.isInCart()){
      throw new ReviewCannotWriteInCartException();
    }

    if(customProduct.getOrders().getOrderStatus() != OrderStatus.COMPLETE){
      throw new ReviewCannotWriteByOrderStatusIsNotCompleteException();
    }

    if (reviewRepository.findByCustomProduct(customProduct).isPresent()) {
      throw new ReviewAlreadyExistByCustomProductException();
    }

    Review review = Review.create(dto.getRating(), dto.getContent());
    review.associate(customProduct);

    if (images != null){
      addImage(images, review);
    }
    reviewRepository.save(review);
  }
  private void addImage(List<MultipartFile> imageFiles, Review review) {
    if (imageFiles.size() > Review.IMAGES_MAX_COUNT)
      throw new BadRequestException(1 + " <= Size of images <= " + Review.IMAGES_MAX_COUNT);
    for (MultipartFile imageFile : imageFiles) {
      String reviewImageUrl = s3Uploader.upload(imageFile);
      review.addImage(ReviewImage.create(review, reviewImageUrl));
    }
  }
}
