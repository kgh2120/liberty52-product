package com.liberty52.product.service.entity;

import com.liberty52.product.global.exception.internal.InvalidRatingException;
import com.liberty52.product.global.exception.internal.InvalidReviewImageSize;
import com.liberty52.product.global.exception.internal.InvalidTextSize;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    private String id = UUID.randomUUID().toString();
    @Column(nullable = false)
    private Integer rating;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImage> reviewImages = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Orders order;

    public void associate(Product product) {
        this.product = product;
        product.addReview(this);
    }

    public void associate(Orders order) {
        this.order = order;
    }

    public void addImage(ReviewImage reviewImage) {
        this.reviewImages.add(reviewImage);
        validImages();
    }

    public void addReply(Reply reply) {
        this.replies.add(reply);
    }

    private void validRating() {
        if (this.rating < 0 || this.rating > 5) {
            throw new InvalidRatingException();
        }
    }

    private void validContent() {
        if(this.content.length() > 1000) {
            throw new InvalidTextSize();
        }
    }

    private void validImages() {
        if(this.reviewImages.size() > 10) {
            throw new InvalidReviewImageSize();
        }
    }
}
