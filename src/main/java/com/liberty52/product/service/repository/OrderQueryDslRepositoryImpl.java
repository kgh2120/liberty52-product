package com.liberty52.product.service.repository;

import static com.liberty52.product.service.entity.QCustomProduct.customProduct;
import static com.liberty52.product.service.entity.QCustomProductOption.customProductOption;
import static com.liberty52.product.service.entity.QOptionDetail.optionDetail;
import static com.liberty52.product.service.entity.QOrderDestination.orderDestination;
import static com.liberty52.product.service.entity.QOrders.orders;
import static com.liberty52.product.service.entity.QProduct.product;
import static com.liberty52.product.service.entity.payment.QPayment.*;

import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.payment.QPayment;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Transactional
@Repository
public class OrderQueryDslRepositoryImpl implements OrderQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;


    @Autowired
    public OrderQueryDslRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
    }

    public List<Orders> retrieveOrders(String authId) {

        return selectOrdersAndAssociatedEntity()
                .where(orders.authId.eq(authId).and(orders.orderStatus.ne(OrderStatus.READY)))
                .orderBy(orders.orderDate.desc())
                .fetch();

    }

    @Override
    public Optional<Orders> retrieveOrderDetail(String authId,
            String orderId) {

       return Optional.ofNullable(
               selectOrdersAndAssociatedEntity()
               .where(orders.authId.eq(authId).and(orders.id.eq(orderId)).and(orders.orderStatus.ne(OrderStatus.READY)))
               .fetchOne());
    }

    @Override
    public Optional<Orders> retrieveGuestOrderDetail(String guestId, String orderNumber) {



        return Optional.ofNullable(
                selectOrdersAndAssociatedEntity()
                .where(orders.authId.eq(guestId).and(orders.orderNum.eq(orderNumber)).and(orders.orderStatus.ne(OrderStatus.READY)))
                .fetchOne());
    }

    private JPAQuery<Orders> selectOrdersAndAssociatedEntity() {
        return queryFactory
                .selectFrom(orders)
                .leftJoin(orderDestination).on(orders.orderDestination.eq(orderDestination))
                .fetchJoin()
                .leftJoin(customProduct).on(customProduct.orders.eq(orders)).fetchJoin()
                .leftJoin(product).on(customProduct.product.eq(product)).fetchJoin()
                .leftJoin(customProductOption).on(customProductOption.customProduct.eq(
                        customProduct)).fetchJoin()
                .leftJoin(optionDetail).on(customProductOption.optionDetail.eq(optionDetail))
                .leftJoin(payment).on(payment.orders.eq(orders)).fetchJoin();
    }


}
