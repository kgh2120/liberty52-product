package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.adapter.cloud.AuthServiceClient;
import com.liberty52.product.global.exception.external.badrequest.CannotAccessOrderException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.OrderRetrieveService;
import com.liberty52.product.service.controller.dto.*;
import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.repository.OrderQueryDslRepository;
import com.liberty52.product.service.repository.OrderQueryDslRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderRetrieveServiceImpl implements OrderRetrieveService {

    private final OrderQueryDslRepository orderQueryDslRepository;
    private final AuthServiceClient authServiceClient;

    @Override
    public List<OrdersRetrieveResponse> retrieveOrders(String authId) {
        return orderQueryDslRepository.retrieveOrders(authId)
                .stream().map(OrdersRetrieveResponse::new).toList();
    }

    @Override
    public OrderDetailRetrieveResponse retrieveOrderDetail(String authId, String orderId) {
        Orders orders = orderQueryDslRepository.retrieveOrderDetail(authId, orderId)
                .orElseThrow(CannotAccessOrderException::new);
        return new OrderDetailRetrieveResponse(orders);
    }

    @Override
    public OrderDetailRetrieveResponse retrieveGuestOrderDetail(String authId, String orderNumber) {
        Orders orders = orderQueryDslRepository.retrieveGuestOrderDetail(authId, orderNumber)
                .orElseThrow(CannotAccessOrderException::new);
        return new OrderDetailRetrieveResponse(orders);
    }

    @Override
    public AdminOrderListResponse retrieveOrdersByAdmin(String role, Pageable pageable) {
        Validator.isAdmin(role);

        List<Orders> orders = orderQueryDslRepository.retrieveOrdersByAdmin(pageable);
        if (CollectionUtils.isEmpty(orders)) {
            return AdminOrderListResponse.empty();
        }
        OrderQueryDslRepositoryImpl.PageInfo pageInfo = orderQueryDslRepository.getPageInfo(pageable);

        Set<String> customerIds = orders.stream().map(Orders::getAuthId).collect(Collectors.toSet());
        Map<String, AuthClientDataResponse> customerInfos = authServiceClient.retrieveAuthData(customerIds);

        return AdminOrderListResponse.of(
                orders,
                customerInfos,
                pageInfo.getCurrentPage(),
                pageInfo.getStartPage(),
                pageInfo.getLastPage(),
                pageInfo.getTotalLastPage()
        );
    }

    @Override
    public OrderDetailRetrieveResponse retrieveOrderDetailByAdmin(String role, String orderId) {
        Validator.isAdmin(role);

        Orders order = orderQueryDslRepository.retrieveOrderDetailByOrderId(orderId)
                .orElseThrow(CannotAccessOrderException::new);

        String customerId = order.getAuthId();
        String customerName = authServiceClient.retrieveAuthData(Set.of(customerId))
                .get(customerId).getAuthorName();

        return OrderDetailRetrieveResponse.of(order, customerName);
    }

    @Override
    public AdminCanceledOrderListResponse retrieveCanceledOrdersByAdmin(String role, Pageable pageable) {
        Validator.isAdmin(role);
        List<Orders> orders = orderQueryDslRepository.retrieveCanceledOrdersByAdmin(pageable);
        return getAdminCanceledOrderListResponse(pageable, orders, OrderStatus.CANCELED, OrderStatus.CANCEL_REQUESTED);
    }

    @Override
    public AdminCanceledOrderListResponse retrieveOnlyRequestedCanceledOrdersByAdmin(String role, Pageable pageable) {
        Validator.isAdmin(role);
        List<Orders> orders = orderQueryDslRepository.retrieveOnlyRequestedCanceledOrdersByAdmin(pageable);
        return getAdminCanceledOrderListResponse(pageable, orders, OrderStatus.CANCEL_REQUESTED);
    }

    @Override
    public AdminCanceledOrderListResponse retrieveOnlyCanceledOrdersByAdmin(String role, Pageable pageable) {
        Validator.isAdmin(role);
        List<Orders> orders = orderQueryDslRepository.retrieveOnlyCanceledOrdersByAdmin(pageable);
        return getAdminCanceledOrderListResponse(pageable, orders, OrderStatus.CANCELED);
    }

    @Override
    public AdminCanceledOrderDetailResponse retrieveCanceledOrderDetailByAdmin(String role, String orderId) {
        Validator.isAdmin(role);

        Orders order = orderQueryDslRepository.retrieveOrderDetailWithCanceledOrdersByAdmin(orderId)
                .orElseThrow(CannotAccessOrderException::new);

        String customerId = order.getAuthId();
        String customerName = authServiceClient.retrieveAuthData(Set.of(customerId))
                .get(customerId).getAuthorName();

        return AdminCanceledOrderDetailResponse.of(order, customerName);
    }

    private AdminCanceledOrderListResponse getAdminCanceledOrderListResponse(Pageable pageable, List<Orders> orders, OrderStatus... statuses) {
        if (CollectionUtils.isEmpty(orders)) {
            return AdminCanceledOrderListResponse.empty();
        }
        OrderQueryDslRepositoryImpl.PageInfo pageInfo = orderQueryDslRepository.getCanceledOrdersPageInfo(pageable, statuses);

        Set<String> customerIds = orders.stream().map(Orders::getAuthId).collect(Collectors.toSet());
        Map<String, AuthClientDataResponse> customerInfos = authServiceClient.retrieveAuthData(customerIds);

        return AdminCanceledOrderListResponse.of(
                orders,
                customerInfos,
                pageInfo.getCurrentPage(),
                pageInfo.getStartPage(),
                pageInfo.getLastPage(),
                pageInfo.getTotalLastPage()
        );
    }

}
