package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.adapter.cloud.AuthServiceClient;
import com.liberty52.product.global.exception.external.badrequest.CannotAccessOrderException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.controller.dto.AdminOrderListResponse;
import com.liberty52.product.service.controller.dto.AuthClientDataResponse;
import com.liberty52.product.service.controller.dto.OrderDetailRetrieveResponse;
import com.liberty52.product.service.controller.dto.OrdersRetrieveResponse;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.repository.OrderQueryDslRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.liberty52.product.service.repository.OrderQueryDslRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


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
}
