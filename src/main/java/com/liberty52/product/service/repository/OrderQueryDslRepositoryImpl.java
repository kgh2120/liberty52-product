package com.liberty52.product.service.repository;

import static com.liberty52.product.service.entity.QCustomProduct.customProduct;
import static com.liberty52.product.service.entity.QOrderDestination.orderDestination;
import static com.liberty52.product.service.entity.QOrders.orders;
import static com.liberty52.product.service.entity.QProduct.product;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.group.GroupBy.sum;

import com.liberty52.product.service.controller.dto.OrderDetailRetrieveResponse;
import com.liberty52.product.service.controller.dto.OrdersRetrieveResponse;
import com.liberty52.product.service.controller.dto.QOrderDetailRetrieveResponse;
import com.liberty52.product.service.controller.dto.QOrderRetrieveProductResponse;
import com.liberty52.product.service.controller.dto.QOrdersRetrieveResponse;
import com.querydsl.jpa.JPQLTemplates;
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
public class OrderQueryDslRepositoryImpl implements OrderQueryDslRepository{

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;


    @Autowired
    public OrderQueryDslRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT,em);
    }

    public List<OrdersRetrieveResponse> retrieveOrders(String authId) {

        return queryFactory
                .from(orders)
                .join(orderDestination).on(orderDestination.orders.id.eq(orders.id))
                .join(customProduct).on(customProduct.orders.id.eq(orders.id))
                .join(product).on(customProduct.product.id.eq(product.id))
                .where(orders.authId.eq(authId))
                .orderBy(orders.orderDate.desc())
                .transform(groupBy(orders.id).as(new QOrdersRetrieveResponse(
                        orders.id,
                        orders.orderDate.stringValue(),
                        orders.orderStatus.stringValue(),
                        orderDestination.address1.append(" ").append(orderDestination.address2),
                        orderDestination.receiverName,
                        orderDestination.receiverEmail,
                        orderDestination.receiverPhoneNumber,
                        list(new QOrderRetrieveProductResponse(
                                product.name, customProduct.quantity, product.price
                        ))
                ))).values().stream().toList();

    }

    @Override
    public Optional<OrderDetailRetrieveResponse> retrieveOrderDetail(String authId, String orderId) {
        return Optional.ofNullable(queryFactory
                .from(orders)
                .join(orderDestination).on(orderDestination.orders.id.eq(orders.id))
                .join(customProduct).on(customProduct.orders.id.eq(orders.id))
                .join(product).on(customProduct.product.id.eq(product.id))
                .where(orders.authId.eq(authId).and(orders.id.eq(orderId)))
                .transform(groupBy(orders.id).as(
                        (new QOrderDetailRetrieveResponse(
                                orders.id,
                                orders.orderDate.stringValue(),
                                orders.orderStatus.stringValue(),
                                orderDestination.address1.append(" ").append(orderDestination.address2),
                                orderDestination.receiverName,
                                orderDestination.receiverEmail,
                                orderDestination.receiverPhoneNumber,
                                sum(product.price),
                                orders.deliveryPrice,
                                list(new QOrderRetrieveProductResponse(
                                        product.name, customProduct.quantity, product.price,
                                        customProduct.userCustomPictureUrl
                                ))
                        )
                        ))).get(orderId));

    }


}
