package com.liberty52.product.service.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.entity.ReviewImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data @AllArgsConstructor
public class AdminReviewRetrieveResponse {
  private List<ReviewContent> contents;
  private long currentPage;
  private long startPage;
  private long lastPage;
  private long totalLastPage;

  @JsonIgnore
  private Set<String> authorIds = new HashSet<>();
  public AdminReviewRetrieveResponse(List<Review> reviews, long currentPage, long startPage, long lastPage, long totalLastPage) {
    contents = reviews.stream().map(r -> new ReviewContent(r.getId(),r.getRating(), r.getContent(),
        r.getReviewImages().stream().map(ReviewImage::getUrl).toList())).toList();

    this.currentPage = currentPage;
    this.startPage = startPage;
    this.lastPage = lastPage;
    this.totalLastPage = totalLastPage;
  }
  @Data
  public class ReviewContent{
    private String reviewId;
    private Integer rating;
    private String content;
    private List<String> imageUrls;

    public ReviewContent(String reviewId, Integer rating, String content, List<String> imageUrls) {
      this.reviewId = reviewId;
      this.rating = rating;
      this.content = content;
      this.imageUrls = imageUrls;
    }
  }
}
