package com.liberty52.product.service.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liberty52.product.service.controller.dto.ReviewRetrieveResponse.ReplyContent;
import com.liberty52.product.service.controller.dto.ReviewRetrieveResponse.ReviewContent;
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
      String orderAuthId = r.getOrder().getAuthId();
      ReviewContent content = new ReviewContent(r.getId(),r.getRating(), r.getContent(),
          r.getReviewImages().stream().map(
              ReviewImage::getUrl).toList(), r.getReplies().size(),
          orderAuthId, r.getReplies().stream()
          .map(rp -> new ReplyContent(rp.getAuthId(), rp.getContent(), rp.getId())).toList()
      );
      authorIds.add(orderAuthId);
      content.replies.forEach(rp -> authorIds.add(rp.authorId));
      return content;
    }).toList();

    this.currentPage = currentPage;
    this.startPage = startPage;
    this.lastPage = lastPage;
    this.totalLastPage = totalLastPage;
  }

  public void setReviewAuthor( Map<String, AuthClientDataResponse> reviewAuthorId){
    contents.forEach(c -> {
      c.authorName = reviewAuthorId.get(c.authorId).getAuthorName();
      c.authorProfileUrl = reviewAuthorId.get(c.authorId).getAuthorProfileUrl();
      c.setReplyAuthor(reviewAuthorId);
    });
  }
  @Data
  public class ReviewContent{


    private String reviewId;
    private Integer rating;
    private String content;
    private List<String> imageUrls;
    private int nOfReply;
    @JsonIgnore
    private String authorId;
    private String authorName; // openfeign으로 채울 값
    private String authorProfileUrl;// openfeign으로 채울 값
    private List<ReplyContent> replies;

    public ReviewContent(String reviewId, Integer rating, String content, List<String> imageUrls, int nOfReply,
        String authorId, List<ReplyContent> replies) {
      this.reviewId = reviewId;
      this.rating = rating;
      this.content = content;
      this.imageUrls = imageUrls;
      this.nOfReply = nOfReply;
      this.authorId = authorId;
      this.replies = replies;
    }

    public void setReplyAuthor( Map<String, AuthClientDataResponse> map){
      replies.forEach(r -> {
        AuthClientDataResponse data = map.get(r.authorId);
        r.authorName = data.getAuthorName();
        r.authorProfileUrl = data.getAuthorProfileUrl();
      });
    }

  }
  @Data
  public class ReplyContent{
    @JsonIgnore
    private String authorId;
    private String authorName; // openFeign으로 채울 값
    private String authorProfileUrl; // openFeign으로 채울 값
    private String content;
    private String replyId;

    public ReplyContent(String authorId, String content,String replyId) {
      this.authorId = authorId;
      this.content = content;
      this.replyId = replyId;
    }
  }
}
