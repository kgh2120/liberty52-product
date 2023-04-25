package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.config.DBInitConfig;
import com.liberty52.product.global.exception.internal.InvalidRatingException;
import com.liberty52.product.global.exception.internal.InvalidTextSize;
import com.liberty52.product.service.controller.dto.ReviewImagesRemoveRequestDto;
import com.liberty52.product.service.controller.dto.ReviewModifyRequestDto;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.entity.ReviewImage;
import com.liberty52.product.service.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReviewModifyServiceImplTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewModifyService reviewModifyService;

    private Review review;
    private Product product;
    private Orders order;
    private List<ReviewImage> images;

    String authId = DBInitConfig.DBInitService.AUTH_ID;
    MockMultipartFile imageFile = newImageFile();


    private MockMultipartFile newImageFile() {
        try {
            return new MockMultipartFile("image", UUID.randomUUID().toString() + ".jpg", "image/jpeg", new FileInputStream("src/test/resources/static/test.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void beforeEach() {
        product = DBInitConfig.DBInitService.getProduct();
        order = DBInitConfig.DBInitService.getOrder();

        review = Review.create(4, "content");
        review.associate(order);
        review.associate(product);
        images = IntStream.range(0, 4).mapToObj(i -> ReviewImage.create(review, "url" + i)).toList();
        reviewRepository.save(review);
    }

    @Test
    void modifyReviewRatingContent() {
        Integer rating = review.getRating() % 5 + 1;
        String content = UUID.randomUUID().toString();
        reviewModifyService.modifyRatingContent(authId, review.getId(), ReviewModifyRequestDto.createForTest(rating, content));

        Review review = reviewRepository.findById(this.review.getId()).get();
        Assertions.assertEquals(rating, review.getRating());
        Assertions.assertEquals(content, review.getContent());
    }

    @Test
    void modifyReviewRatingValidation() {
        Integer invalidRatingOverMaxValue = Review.RATING_MAX_VALUE + 1;
        String content = UUID.randomUUID().toString();
        Assertions.assertThrows(InvalidRatingException.class, () -> reviewModifyService.modifyRatingContent(authId, review.getId(), ReviewModifyRequestDto.createForTest(invalidRatingOverMaxValue, content)));

        Integer invalidRatingBelowMinValue = Review.RATING_MIN_VALUE - 1;
        Assertions.assertThrows(InvalidRatingException.class, () -> reviewModifyService.modifyRatingContent(authId, review.getId(), ReviewModifyRequestDto.createForTest(invalidRatingBelowMinValue, content)));

        Integer validRating = 1;
        String invalidContentOver = "a".repeat(Review.CONTENT_MAX_LENGTH + 3);
        Assertions.assertThrows(InvalidTextSize.class, () -> reviewModifyService.modifyRatingContent(authId, review.getId(), ReviewModifyRequestDto.createForTest(validRating, invalidContentOver)));

        String invalidContentBelow = "";
        Assertions.assertThrows(InvalidTextSize.class, () -> reviewModifyService.modifyRatingContent(authId, review.getId(), ReviewModifyRequestDto.createForTest(validRating, invalidContentBelow)));
    }

    @Test
    void addImages() {
        int addCount = 2;
        int sum = review.getReviewImages().size() + addCount;
        reviewModifyService.addImages(authId, review.getId(), IntStream.range(0, addCount).mapToObj(i -> newImageFile()).toList());
        Review review1 = reviewRepository.findById(review.getId()).get();
        Assertions.assertEquals(sum, review1.getReviewImages().size());
    }

    @Test
    void addImagesValidation() {
        int addCount = Review.IMAGES_MAX_COUNT;
        int sum = review.getReviewImages().size() + addCount;
        reviewModifyService.addImages(authId, review.getId(), IntStream.range(0, addCount).mapToObj(i -> newImageFile()).toList());
        Review review1 = reviewRepository.findById(review.getId()).get();
        Assertions.assertEquals(Math.min(Review.IMAGES_MAX_COUNT, sum), review1.getReviewImages().size());
    }

    @Test
    void removeImages() {
        int removeCount = 2;
        int expected = review.getReviewImages().size() - removeCount;
        List<String> removeList = images.subList(0, 2).stream().map(ReviewImage::getUrl).toList();
        reviewModifyService.removeImages(authId, review.getId(), ReviewImagesRemoveRequestDto.createForTest(removeList));
        Review review1 = reviewRepository.findById(review.getId()).get();

        Assertions.assertEquals(expected, review1.getReviewImages().size());
        System.out.println(removeList);
        for (ReviewImage reviewImage : review1.getReviewImages()) {
            Assertions.assertFalse(removeList.contains(reviewImage.getUrl()));
        }
    }

    @Test
    void modifyReview() {
        Integer rating = review.getRating() % 5 + 1;
        String content = UUID.randomUUID().toString();
        int addCount = Review.IMAGES_MAX_COUNT;
        reviewModifyService.modifyReview(authId, review.getId(), ReviewModifyRequestDto.createForTest(rating, content), IntStream.range(0, addCount).mapToObj(i -> newImageFile()).toList());

        Review review = reviewRepository.findById(this.review.getId()).get();
        Assertions.assertEquals(rating, review.getRating());
        Assertions.assertEquals(content, review.getContent());
        Assertions.assertEquals(addCount, review.getReviewImages().size());
    }
}