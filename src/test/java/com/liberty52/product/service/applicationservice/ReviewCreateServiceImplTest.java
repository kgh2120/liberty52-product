package com.liberty52.product.service.applicationservice;

import com.liberty52.product.MockS3Test;
import com.liberty52.product.service.controller.dto.ReviewCreateRequestDto;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.repository.CustomProductRepository;
import com.liberty52.product.service.repository.ReviewRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReviewCreateServiceImplTest extends MockS3Test {
  @Autowired
  private CustomProductRepository customProductRepository;
  @Autowired
  private ReviewRepository reviewRepository;
  @Autowired
  private ReviewCreateService reviewCreateService;
  private final List<MultipartFile> testImageList = new ArrayList<>();
  private final String reviewerId = "authId2";
  private final MockMultipartFile imageFile = newImageFile();

  private MockMultipartFile newImageFile() {
    try {
      return new MockMultipartFile("image", UUID.randomUUID() + ".jpg", "image/jpeg",
          new FileInputStream("src/test/resources/static/test.jpg"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  @Order(1)
  void createReview() {
    final String MOCK_CUSTOM_PRODUCT_ID = "Custom_Product_Id";
    CustomProduct customProduct = customProductRepository.findById(MOCK_CUSTOM_PRODUCT_ID).get();
    Integer rating = 3;
    String content = "is very nice review";
    testImageList.add(imageFile);

    ReviewCreateRequestDto dto = ReviewCreateRequestDto.createForTest(rating, content,customProduct.getId());

    reviewCreateService.createReview(reviewerId,dto,testImageList);
    Review review = reviewRepository.findByCustomProduct_Orders(customProduct.getOrders()).get();

    Assertions.assertEquals(rating, review.getRating());
    Assertions.assertEquals(content, review.getContent());
  }

}