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
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.BatchSize;

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
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Orders order;

    public static final int CONTENT_MAX_LENGTH = 1000;
    public static final int CONTENT_MIN_LENGTH = 1;
    public static final int RATING_MAX_VALUE = 5;
    public static final int RATING_MIN_VALUE = 1;
    public static final int IMAGES_MAX_COUNT = 10;

    public static Review create(int rating, String content) {
        Review review = new Review();
        review.rating = rating;
        review.validRating();
        review.content = content;
        review.validContent();
        return review;
    }

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
        if (this.rating < RATING_MIN_VALUE || this.rating > RATING_MAX_VALUE) {
            throw new InvalidRatingException();
        }
    }

    private void validContent() {
        if(this.content.length() < CONTENT_MIN_LENGTH || this.content.length() > CONTENT_MAX_LENGTH) {
            throw new InvalidTextSize();
        }
    }

    private void validImages() {
        if(this.reviewImages.size() > IMAGES_MAX_COUNT) {
            throw new InvalidReviewImageSize();
        }
    }

    public void modify(Integer rating, String content) {
        this.rating = rating;
        this.content = content;
        validRating();
        validContent();
    }

    public void removeImagesByUrl(Set<String> urls) {
        this.reviewImages.removeIf(img -> urls.contains(img.getUrl()));
    }

    public boolean isImageAddable() {
        return this.reviewImages.size() + 1 <= IMAGES_MAX_COUNT;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", rating=" + rating +
                ", content='" + content + '\'' +
                ", replies=" + replies +
                ", reviewImages=" + reviewImages +
                ", product=" + product +
                ", order=" + order +
                '}';
    }

    public void clearImages() {
        this.reviewImages.clear();
    }

    public void removeImage(ReviewImage reviewImage) {
        this.reviewImages.remove(reviewImage);
    }
}
