package com.liberty52.product.service.repository;

import static com.liberty52.product.service.entity.QOrders.*;

import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.utils.MockFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class JpaQueryFactoryTest {
    @Autowired
    EntityManager em;



    @Autowired
    OrdersRepository ordersRepository;

    @Test
    void test () throws Exception{
        //given

        JPAQueryFactory qf = new JPAQueryFactory(em);

        String authId = "1234";
        Orders order = MockFactory.createOrder(authId, null);
        ordersRepository.save(order);


        //when
        Orders finded = qf.selectFrom(orders)
                .where(orders.authId.eq(authId))
                .fetchOne();
        //then

        Assertions.assertThat(finded.getAuthId())
                .isEqualTo(authId);
    }
}