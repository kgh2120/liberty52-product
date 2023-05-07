package com.liberty52.product.service.repository;

import static com.liberty52.product.service.entity.QOrders.orders;
import static com.liberty52.product.service.entity.QReply.reply;
import static com.liberty52.product.service.entity.QReview.review;
import static com.liberty52.product.service.entity.QReviewImage.reviewImage;

import com.liberty52.product.service.controller.dto.AdminReviewRetrieveResponse;
import com.liberty52.product.service.controller.dto.ReviewRetrieveResponse;
import com.liberty52.product.service.entity.Review;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Repository
public class ReviewQueryDslRepositoryImpl implements ReviewQueryDslRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private final String currentPage = "currentPage";
    private final String startPage = "startPage";
    private final String lastPage = "lastPage";
    private final String totalLastPage = "totalLastPage";

    public ReviewQueryDslRepositoryImpl(EntityManager em){
        this.em = em;
        this.queryFactory = new JPAQueryFactory(this.em);
    }

    public ReviewRetrieveResponse retrieveReview(String productId, String authorId, Pageable pageable,
            boolean isPhotoFilter){

        List<Review> reviews = fetchReviews(productId, pageable,isPhotoFilter);

        if(reviews.isEmpty())
            return noReviewExistCase(authorId, reviews);

        Map<String, Long> pageInfo = getPageInfo(pageable, productId);

        return new ReviewRetrieveResponse(reviews,
                pageInfo.get(startPage),
                pageInfo.get(currentPage),
                pageInfo.get(lastPage),
                pageInfo.get(totalLastPage)
                ,authorId);
    }

    @Override
    public List<Review> retrieveReviewByWriterId(String writerId) {
        return queryFactory
                .selectFrom(review)
                .leftJoin(orders).on(orders.eq(review.order)).fetchJoin()
                .leftJoin(reviewImage).on(reviewImage.review.eq(review))
                .where(orders.authId.eq(writerId))
                .fetch();

    }

    @Override
    public AdminReviewRetrieveResponse retrieveAllReviews(Pageable pageable) {
        List<Review> reviews = fetchReviews(pageable);
        if(reviews.isEmpty())
            return noReviewExistCase(reviews);
        Map<String, Long> pageInfo = getPageInfo(pageable);
        return new AdminReviewRetrieveResponse(reviews,
            pageInfo.get(startPage),
            pageInfo.get(currentPage),
            pageInfo.get(lastPage),
            pageInfo.get(totalLastPage));
    }

    private List<Review> fetchReviews(String productId, Pageable pageable, Boolean isPhotoFilter) {
        return queryFactory.selectFrom(review).distinct()
            .leftJoin(reply).on(reply.review.eq(review))
            .leftJoin(reviewImage).on(reviewImage.review.eq(review))
            .leftJoin(orders).on(review.order.eq(orders))
            .where(productIdFilter(productId), photoFilter(isPhotoFilter))
            .orderBy(review.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    private List<Review> fetchReviews(Pageable pageable) {
        return fetchReviews(null, pageable, false);
    }

    private BooleanExpression productIdFilter(String productId) {
        if (productId != null) {
            return review.product.id.eq(productId);
        }
        return null;
    }

    private Long getTotalCount(String productId) {
        return queryFactory.select(review.count())
                .from(review)
                .where(productIdFilter(productId))
                .fetchOne();
    }


    private BooleanExpression photoFilter(boolean isPhotoFilter){
        if (!isPhotoFilter) {
            return null;
        }

        return review.reviewImages.size().gt(0);

    }

    private Map<String,Long> getPageInfo(Pageable pageable, String productId){
        long currentPage = pageable.getPageNumber() +1;
        long startPage =  currentPage %10 ==0 ? (currentPage/10-1)*10+1 : (currentPage/10)*10 +1;
        Long total = getTotalCount(productId);
        long totalLastPage = total%pageable.getPageSize() == 0 ? total / pageable.getPageSize() :
                total / pageable.getPageSize()+1;
        long lastPage = Math.min(totalLastPage,
                10L * (currentPage%10 == 0 ? currentPage/10 : currentPage / 10 + 1));
        Map<String,Long> returnMap = new HashMap<>();
        returnMap.put(this.startPage,startPage);
        returnMap.put(this.currentPage,currentPage);
        returnMap.put(this.lastPage,lastPage);
        returnMap.put(this.totalLastPage, totalLastPage);
        return returnMap;
    }

    private Map<String,Long> getPageInfo(Pageable pageable){
        return getPageInfo(pageable,null);
    }

    private ReviewRetrieveResponse noReviewExistCase(String authorId,
            List<Review> reviews) {
        return new ReviewRetrieveResponse(reviews, 0, 0, 0, 0,authorId);
    }

    private AdminReviewRetrieveResponse noReviewExistCase(List<Review> reviews) {
        return new AdminReviewRetrieveResponse(reviews, 0, 0, 0, 0);
    }

}
