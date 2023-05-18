package com.liberty52.product.service.controller.dto;

import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.entity.ReviewImage;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class AdminReviewDetailResponse {
  private ReviewContent content;

  public AdminReviewDetailResponse(Review review) {
    String orderAuthId = review.getCustomProduct().getOrders().getAuthId();
    content = new ReviewContent(review.getRating(),review.getContent(),review.getReviewImages().stream().map(
        ReviewImage::getUrl).toList(),review.getCreatedAt().toLocalDate(),orderAuthId,review.getReplies().stream().map(
            rp -> new ReplyContent(rp.getAuthId(),rp.getContent(),rp.getId(),rp.getCreatedAt().toLocalDate())).toList());
  }

  @Data
  public class ReviewContent{
    private Integer rating;
    private String content;
    private List<String> imageUrls;
    private String authorId;
    private LocalDate reviewCreatedAt;
    private List<ReplyContent> replies;

    public ReviewContent(Integer rating, String content, List<String> imageUrls, LocalDate reviewCreatedAt,
        String authorId, List<ReplyContent> replies) {
      this.rating = rating;
      this.content = content;
      this.imageUrls = imageUrls;
      this.authorId = authorId;
      this.reviewCreatedAt = reviewCreatedAt;
      this.replies = replies;
    }
  }

  @Data
  public class ReplyContent{
    private String authorId;
    private String content;
    private String replyId;
    private LocalDate replyCreatedAt;

    public ReplyContent(String authorId, String content,String replyId, LocalDate replyCreatedAt) {
      this.authorId = authorId;
      this.content = content;
      this.replyId = replyId;
      this.replyCreatedAt = replyCreatedAt;
    }
  }
}
