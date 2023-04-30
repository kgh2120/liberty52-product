package com.liberty52.product.service.repository;

import com.liberty52.product.global.config.DBInitConfig;
import com.liberty52.product.global.config.DBInitConfig.DBInitService;
import com.liberty52.product.service.controller.dto.ReviewRetrieveResponse;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ReviewQueryDslRepositoryImplTest {


    @Autowired
    EntityManager em;

    @Autowired
    DBInitConfig config;

    JPAQueryFactory qf;
    Orders order;
    Product product;

    @Autowired
    ReviewQueryDslRepository reviewQueryDslRepository;

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
        ReviewRetrieveResponse response = reviewQueryDslRepository.retrieveReview(
                product.getId(), order.getAuthId(), PageRequest.of(0, 3), true);
        //when
        assertThat(response.getContents().size()).isSameAs(1);
    }
    @Test
    void photoFilterTest_NotFiltering () throws Exception{
        //given
        PageRequest request = PageRequest.of(0, 10);
        ReviewRetrieveResponse response = reviewQueryDslRepository.retrieveReview(
                product.getId(), order.getAuthId(), request, false);

        assertThat(response.getContents().size()).isSameAs(10);
    }

}