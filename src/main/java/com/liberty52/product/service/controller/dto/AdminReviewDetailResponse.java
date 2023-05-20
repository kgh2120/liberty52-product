package com.liberty52.product.service.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.entity.ReviewImage;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class AdminReviewDetailResponse {
  private ReviewContent content;

  @JsonIgnore
  private Set<String> authorIds = new HashSet<>();
  public AdminReviewDetailResponse(Review review) {
    String orderAuthId = review.getCustomProduct().getOrders().getAuthId();
    authorIds.add(orderAuthId);
    content = new ReviewContent(review.getRating(),review.getContent(),review.getReviewImages().stream().map(
        ReviewImage::getUrl).toList(),review.getCreatedAt().toLocalDate(),orderAuthId,review.getReplies().stream().map(
            rp -> new ReplyContent(rp.getAuthId(),rp.getContent(),rp.getId(),rp.getCreatedAt().toLocalDate())).toList());
  }

  public void setReviewAuthor( Map<String, AuthClientDataResponse> reviewAuthorId){
    content.authorName = reviewAuthorId.get(content.authorId).getAuthorName();
    content.setReplyAuthor(reviewAuthorId);
  }

  @Data
  public class ReviewContent{
    private Integer rating;
    private String content;
    private List<String> imageUrls;
    private String authorId;
    public String authorName;
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

    public void setReplyAuthor( Map<String, AuthClientDataResponse> map){
      replies.forEach(r -> {
        AuthClientDataResponse data = map.get(r.authorId);
        r.authorName = data.getAuthorName();
      });
    }
  }

  @Data
  public class ReplyContent{
    private String authorId;
    private String authorName;
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
