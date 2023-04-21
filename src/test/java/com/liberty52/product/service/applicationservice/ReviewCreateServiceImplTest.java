package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.ReplyCreateRequestDto;
import com.liberty52.product.service.controller.dto.ReviewCreateRequestDto;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.OrderDestination;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.Reply;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.repository.CustomProductRepository;
import com.liberty52.product.service.repository.OrdersRepository;
import com.liberty52.product.service.repository.ProductRepository;
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
class ReviewCreateServiceImplTest {
  @Autowired
  private OrdersRepository ordersRepository;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private CustomProductRepository customProductRepository;
  @Autowired
  private ReviewRepository reviewRepository;
  @Autowired
  private ReviewCreateService reviewCreateService;
  Orders order;
  CustomProduct customProduct;
  private final List<MultipartFile> testImageList = new ArrayList<>();
  private final String reviewerId = "authId2";

  private final String productName = "Liberty 52_Frame";
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
    order= ordersRepository.save(Orders.create("authId2", 10000, OrderDestination.create("receiver",
        "email", "01012341234", "경기도 어딘가", "101동 101호", "12345")));
    Product product = productRepository.findByName(productName).get();

    customProduct = CustomProduct.create("aa", 3, "authId2");
    customProduct.associateWithProduct(product);
    customProduct.associateWithOrder(order);
    customProduct = customProductRepository.save(customProduct);

    Integer rating = 3;
    String content = "is very nice review";
    testImageList.add(imageFile);

    ReviewCreateRequestDto dto = ReviewCreateRequestDto.createForTest(productName, rating, content);

    reviewCreateService.createReview(reviewerId,dto, testImageList, order.getId());
    Review review = reviewRepository.findByOrderId(order.getId()).get();

    Assertions.assertEquals(rating, review.getRating());
    Assertions.assertEquals(content, review.getContent());

    reviewRepository.save(review);
  }

  @Test
  @Order(2)
  void createReply() {
    createReview();
    String content = "is very nice reply";
    ReplyCreateRequestDto dto = ReplyCreateRequestDto.createForTest(content);

    Review review = reviewRepository.findByOrderId(order.getId()).get();
    reviewCreateService.createReply(reviewerId, dto, review.getId());

    Reply reply = Reply.create(content, reviewerId);
    Assertions.assertEquals(content, reply.getContent());
    Assertions.assertEquals(reviewerId, reply.getAuthId());
  }

}