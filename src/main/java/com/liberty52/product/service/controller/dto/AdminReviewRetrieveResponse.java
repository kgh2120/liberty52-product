package com.liberty52.product.service.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.entity.ReviewImage;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    contents = reviews.stream().map(r -> {
      String orderAuthId = r.getCustomProduct().getOrders().getAuthId();
      ReviewContent content = new ReviewContent(r.getId(),r.getRating(), r.getContent(),
          r.getReviewImages().stream().map(ReviewImage::getUrl).toList());
      authorIds.add(orderAuthId);
      return content;
    }).toList();

    this.currentPage = currentPage;
    this.startPage = startPage;
    this.lastPage = lastPage;
    this.totalLastPage = totalLastPage;
  }

  public void setReviewAuthor( Map<String, AuthClientDataResponse> reviewAuthorId){
    contents.forEach(c -> c.authorName = reviewAuthorId.get(c.authorId).getAuthorName());
  }

  @Data
  public class ReviewContent{
    private String reviewId;
    @JsonIgnore
    private String authorId;
    private String authorName;
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