package com.liberty52.product.service.repository;

import static com.liberty52.product.service.entity.QOrders.orders;
import static com.liberty52.product.service.entity.QReply.reply;
import static com.liberty52.product.service.entity.QReview.review;
import static com.liberty52.product.service.entity.QReviewImage.reviewImage;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.liberty52.product.global.config.DBInitConfig;
import com.liberty52.product.global.config.DBInitConfig.DBInitService;
import com.liberty52.product.service.controller.dto.ReviewRetrieveResponse;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.QOrders;
import com.liberty52.product.service.entity.QProduct;
import com.liberty52.product.service.entity.QReply;
import com.liberty52.product.service.entity.QReview;
import com.liberty52.product.service.entity.QReviewImage;
import com.liberty52.product.service.entity.Review;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ReviewQueryRepositoryImplTest {


    @Autowired
    EntityManager em;

    @Autowired
    DBInitConfig config;

    JPAQueryFactory qf;
    Orders order;
    Product product;

    @Autowired
    ReviewQueryRepository reviewQueryRepository;

    @BeforeEach
    void beforeEach(){
        qf = new JPAQueryFactory(em);
        order = DBInitService.getOrder();
        product = DBInitService.getProduct();
    }

    @AfterEach
    void afterEach(){
        em.clear();
    }
    @Test
    void photoFilterTest_Filtering () throws Exception{
        //given
        ReviewRetrieveResponse response = reviewQueryRepository.retrieveReview(
                product.getId(), order.getAuthId(), PageRequest.of(0, 3), true);
        //when
        assertThat(response.getContents().size()).isSameAs(1);
    }
    @Test
    void photoFilterTest_NotFiltering () throws Exception{
        //given
        PageRequest request = PageRequest.of(0, 10);
        ReviewRetrieveResponse response = reviewQueryRepository.retrieveReview(
                product.getId(), order.getAuthId(), request, false);

        assertThat(response.getContents().size()).isSameAs(2);
    }

}