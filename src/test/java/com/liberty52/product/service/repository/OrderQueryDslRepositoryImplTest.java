package com.liberty52.product.service.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.liberty52.product.service.entity.OrderDestination;
import com.liberty52.product.service.entity.Orders;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderQueryDslRepositoryImplTest {


    @Autowired
    EntityManager em;


    OrderQueryDslRepository repository;
    @BeforeEach
    void beforeEach(){
        repository = new OrderQueryDslRepositoryImpl(em);
    }

    @Test
    void ready_filtering_test () throws Exception{
        //given
        final String AUTH_ID = "user";

        for (int i = 0; i < 10; i++) {

            OrderDestination destination = OrderDestination.create("receiver", "", "", "", "", "");
            em.persist(Orders.create(AUTH_ID,destination));

        }
        OrderDestination destination = OrderDestination.create("receiver", "", "", "", "", "");
        Orders orders = Orders.create(AUTH_ID, destination);
        orders.changeOrderStatusToOrdered();
        em.persist(orders);
        em.flush();

        //when
        List<Orders> filtered = repository.retrieveOrders(AUTH_ID);
        List<Orders> notFiltered = em.createQuery("select o from Orders o where o.authId = :authId",
                        Orders.class)
                .setParameter("authId", AUTH_ID)
                .getResultList();

        //then
        assertThat(filtered).hasSize(1);
        assertThat(notFiltered).hasSize(11);


    }

}