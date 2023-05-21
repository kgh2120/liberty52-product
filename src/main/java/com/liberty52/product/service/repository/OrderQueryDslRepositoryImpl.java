package com.liberty52.product.service.repository;

import com.liberty52.product.global.exception.external.internalservererror.InternalServerErrorException;
import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.liberty52.product.service.entity.QCanceledOrders.canceledOrders;
import static com.liberty52.product.service.entity.QCustomProduct.customProduct;
import static com.liberty52.product.service.entity.QCustomProductOption.customProductOption;
import static com.liberty52.product.service.entity.QOptionDetail.optionDetail;
import static com.liberty52.product.service.entity.QOrderDestination.orderDestination;
import static com.liberty52.product.service.entity.QOrders.orders;
import static com.liberty52.product.service.entity.QProduct.product;
import static com.liberty52.product.service.entity.payment.QPayment.payment;


@Slf4j
@Transactional
@Repository
public class OrderQueryDslRepositoryImpl implements OrderQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private final String currentPage = "currentPage";
    private final String startPage = "startPage";
    private final String lastPage = "lastPage";
    private final String totalLastPage = "totalLastPage";

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

    @Override
    public List<Orders> retrieveOrdersByAdmin(Pageable pageable) {
        return fetchOrdersByAdmin(pageable).fetch();
    }

    @Override
    public Optional<Orders> retrieveOrderDetailByOrderId(String orderId) {
        return Optional.ofNullable(
                selectOrdersAndAssociatedEntity()
                        .where(orders.id.eq(orderId))
                        .fetchOne()
        );
    }

    @Override
    public List<Orders> retrieveCanceledOrdersByAdmin(Pageable pageable) {
        return fetchOrdersWithCanceledOrdersByAdmin(pageable, OrderStatus.CANCELED, OrderStatus.CANCEL_REQUESTED)
                .fetch();
    }

    @Override
    public List<Orders> retrieveOnlyRequestedCanceledOrdersByAdmin(Pageable pageable) {
        return fetchOrdersWithCanceledOrdersByAdmin(pageable, OrderStatus.CANCEL_REQUESTED)
                .fetch();
    }

    @Override
    public List<Orders> retrieveOnlyCanceledOrdersByAdmin(Pageable pageable) {
        return fetchOrdersWithCanceledOrdersByAdmin(pageable, OrderStatus.CANCELED)
                .fetch();
    }

    @Override
    public Optional<Orders> retrieveOrderDetailWithCanceledOrdersByAdmin(String orderId) {
        return Optional.ofNullable(
                selectOrdersAndAssociatedEntityWithCanceledOrders()
                    .where(orders.id.eq(orderId))
                    .fetchOne()
        );
    }


    private JPAQuery<Orders> selectOrdersAndAssociatedEntity() {
        return queryFactory
                .selectFrom(orders)
                .leftJoin(orderDestination).on(orders.orderDestination.eq(orderDestination)).fetchJoin()
                .leftJoin(customProduct).on(customProduct.orders.eq(orders)).fetchJoin()
                .leftJoin(product).on(customProduct.product.eq(product)).fetchJoin()
                .leftJoin(customProductOption).on(customProductOption.customProduct.eq(customProduct)).fetchJoin()
                .leftJoin(optionDetail).on(customProductOption.optionDetail.eq(optionDetail)).fetchJoin()
                .leftJoin(payment).on(payment.orders.eq(orders)).fetchJoin();
    }

    private JPAQuery<Orders> fetchOrdersByAdmin(Pageable pageable) {
        return queryFactory
                .selectFrom(orders)
                .leftJoin(customProduct).on(customProduct.orders.eq(orders))
                .fetchJoin()
                .leftJoin(product).on(customProduct.product.eq(product))
                .where(orders.orderStatus.ne(OrderStatus.READY))
                .orderBy(orders.orderDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    private JPAQuery<Orders> fetchOrdersWithCanceledOrdersByAdmin(Pageable pageable, OrderStatus... statuses) {
        if (statuses.length == 0 || statuses.length > 2) {
            log.error("[LIB-ERROR] 주문 취소 SQL 로직을 잘못 요청하셨습니다.");
            throw new InternalServerErrorException("SQL Error");
        }
        return queryFactory
                .selectFrom(orders)
                .leftJoin(customProduct).on(customProduct.orders.eq(orders)).fetchJoin()
                .leftJoin(product).on(customProduct.product.eq(product)).fetchJoin()
                .where(
                        statuses.length == 2 ?
                                orders.orderStatus.eq(statuses[0]).or(orders.orderStatus.eq(statuses[1])) :
                                orders.orderStatus.eq(statuses[0])
                )
                .leftJoin(canceledOrders).on(canceledOrders.orders.eq(orders))
                .orderBy(orders.orderDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    private JPAQuery<Orders> selectOrdersAndAssociatedEntityWithCanceledOrders() {
        return selectOrdersAndAssociatedEntity()
                .where(orders.orderStatus.eq(OrderStatus.CANCELED).or(orders.orderStatus.eq(OrderStatus.CANCEL_REQUESTED)))
                .leftJoin(canceledOrders).on(canceledOrders.orders.eq(orders)).fetchJoin();
    }

    private Long getTotalCount() {
        return queryFactory
                .select(orders.count())
                .from(orders)
                .where(orders.orderStatus.ne(OrderStatus.READY))
                .fetchOne();
    }

    private Long getCanceledOrdersCount(OrderStatus... statuses) {
        if (statuses.length == 0 || statuses.length > 2) {
            log.error("[LIB-ERROR] 주문 취소 SQL 로직을 잘못 요청하셨습니다.");
            throw new InternalServerErrorException("SQL Error");
        }
        return queryFactory
                .select(orders.count())
                .from(orders)
                .where(statuses.length == 2 ?
                                orders.orderStatus.eq(statuses[0]).or(orders.orderStatus.eq(statuses[1])) :
                                orders.orderStatus.eq(statuses[0]))
                .fetchOne();
    }

    @Override
    public PageInfo getPageInfo(Pageable pageable) {
        if (pageable == null) {
            throw new InternalServerErrorException("Order List Pagination Error");
        }

        long currentPage = pageable.getPageNumber() +1;
        long startPage =  currentPage % 10 ==0 ? (currentPage/10-1)*10+1 : (currentPage/10)*10 +1;
        Long total = getTotalCount();
        long totalLastPage = total % pageable.getPageSize() == 0 ?
                total / pageable.getPageSize() : total / pageable.getPageSize()+1;
        long lastPage = Math.min(totalLastPage,
                10L * (currentPage%10 == 0 ? currentPage/10 : currentPage / 10 + 1));

        return new PageInfo(startPage, currentPage, lastPage, totalLastPage);
    }

    @Override
    public PageInfo getCanceledOrdersPageInfo(Pageable pageable, OrderStatus... statuses) {
        if (pageable == null) {
            throw new InternalServerErrorException("Order List Pagination Error");
        }

        long currentPage = pageable.getPageNumber() +1;
        long startPage =  currentPage % 10 ==0 ? (currentPage/10-1)*10+1 : (currentPage/10)*10 +1;
        Long total = getCanceledOrdersCount(statuses);
        long totalLastPage = total % pageable.getPageSize() == 0 ?
                total / pageable.getPageSize() : total / pageable.getPageSize()+1;
        long lastPage = Math.min(totalLastPage,
                10L * (currentPage%10 == 0 ? currentPage/10 : currentPage / 10 + 1));

        return new PageInfo(startPage, currentPage, lastPage, totalLastPage);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PageInfo {
        private Long startPage;
        private Long currentPage;
        private Long lastPage;
        private Long totalLastPage;
    }

}
