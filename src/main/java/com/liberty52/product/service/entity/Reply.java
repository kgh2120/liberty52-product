package com.liberty52.product.service.entity;

import com.liberty52.product.global.exception.internal.InvalidTextSize;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String authId;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id")
    private Review review;

    private Reply(String content, String authId) {
        this.content = content;
        this.authId = authId;
    }

    public void associate(Review review) {
        this.review = review;
        review.addReply(this);
    }

    private void validContent() {
        if(this.content.length() > 1000) {
            throw new InvalidTextSize();
        }
    }
    public static Reply create(String content, String authId) {
        Reply reply = new Reply();
        reply.content = content;
        reply.authId = authId;
        reply.validContent();
        return reply;
    }

    public void removeReview() {
        review.removeReply(this);
        review = null;
    }

    public void modify(String content) {
        this.content = content;
        validContent();
    }
}
