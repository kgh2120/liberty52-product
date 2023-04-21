package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.adapter.S3UploaderImpl;
import com.liberty52.product.global.exception.external.CustomProductNotFoundExcpetion;
import com.liberty52.product.global.exception.external.NotYourResourceException;
import com.liberty52.product.global.exception.external.OrderNotFoundException;
import com.liberty52.product.global.exception.external.ProductNotFoundException;
import com.liberty52.product.global.exception.external.ResourceNotFoundException;
import com.liberty52.product.service.controller.dto.ReplyCreateRequestDto;
import com.liberty52.product.service.controller.dto.ReviewCreateRequestDto;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.Reply;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.entity.ReviewImage;
import com.liberty52.product.service.repository.CustomProductRepository;
import com.liberty52.product.service.repository.OrdersRepository;
import com.liberty52.product.service.repository.ProductRepository;
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
  private final ProductRepository productRepository;
  private final OrdersRepository ordersRepository;
  private final CustomProductRepository customProductRepository;
  private final S3UploaderImpl s3Uploader;

  @Override
  public void createReview(String reviewerId, ReviewCreateRequestDto dto, List<MultipartFile> imageFiles, String orderId) {
    Product product = productRepository.findByName(dto.getProductName())
        .orElseThrow(() -> new ProductNotFoundException(dto.getProductName()));

    Orders order = ordersRepository.findById(orderId)
        .orElseThrow(OrderNotFoundException::new);

    customProductRepository.findByOrderIdAndProductId(orderId,product.getId())//해당 order가  해당 product를 구매했는지 확인
        .orElseThrow(CustomProductNotFoundExcpetion::new);

    if(!(order.getAuthId().equals(reviewerId))){
      throw new NotYourResourceException(reviewerId,order.getAuthId());
    }

    Review review = Review.create(dto.getRating(), dto.getContent());
    review.associate(product);
    review.associate(order);

    if(!(imageFiles.size() > Review.IMAGES_MAX_COUNT || ( imageFiles.get(0) != null && imageFiles.get(0).getSize()==0))){//이미지를 안 넣을거면 null 값 보내기
      for (MultipartFile imageFile : imageFiles) {
        String reviewImageUrl = s3Uploader.upload(imageFile);
        review.addImage(ReviewImage.create(review, reviewImageUrl));
      }
    }
    reviewRepository.save(review);
  }

  @Override
  public void createReply(String reviewerId, ReplyCreateRequestDto dto, String reviewId) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new ResourceNotFoundException("Review", "ID", reviewId));
    Reply reply = Reply.create(dto.getContent(),reviewerId);
    reply.associate(review);
  }
}