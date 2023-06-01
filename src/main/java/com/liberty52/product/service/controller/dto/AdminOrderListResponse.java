package com.liberty52.product.service.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liberty52.product.global.util.Utils;
import com.liberty52.product.service.entity.Orders;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminOrderListResponse {

    private List<AdminOrderContent> orders;
    private Long currentPage;
    private Long startPage;
    private Long lastPage;
    private Long totalLastPage;

    public static AdminOrderListResponse empty() {
        return new AdminOrderListResponse(List.of(), 0L, 0L, 0L, 0L);
    }

    public static AdminOrderListResponse of(
            List<Orders> entities,
            Map<String, AuthClientDataResponse> customerInfos,
            Long currentPage, Long startPage, Long lastPage, Long totalLastPage
    ) {
        return new AdminOrderListResponse(entities, customerInfos, currentPage, startPage, lastPage, totalLastPage);
    }

    private AdminOrderListResponse(
            List<Orders> entities,
            Map<String, AuthClientDataResponse> customerInfos,
            Long currentPage, Long startPage, Long lastPage, Long totalLastPage
    ) {
        this.orders = entities.stream()
                .map(AdminOrderContent::of)
                .peek(orderContent -> orderContent.setCustomerNames(customerInfos.get(orderContent.getCustomerId()).getAuthorName()))
                .toList();
        this.currentPage = currentPage;
        this.startPage = startPage;
        this.lastPage = lastPage;
        this.totalLastPage = totalLastPage;
    }

    @Getter
    public static class AdminOrderContent {
        private String orderId;
        private String orderNumber;
        private String productName;
        private String orderDate;
        @JsonIgnore
        private String customerId;
        private String customerName;
        private String orderStatus;

        public static AdminOrderContent of(Orders entity) {
            AdminOrderContent response = new AdminOrderContent();
            response.orderId = entity.getId();
            response.orderNumber = entity.getOrderNum();
            response.productName = getProductName(entity);
            response.orderDate = entity.getOrderedAt().format(Utils.DATE_FORMAT_DATE);
            response.customerId = entity.getAuthId();
            response.orderStatus = entity.getOrderStatus().getName();
            return response;
        }

        private void setCustomerNames(String customerName) {
            this.customerName = customerName;
        }

        private static String getProductName(Orders entity) {
            StringBuilder sb = new StringBuilder();
            sb.append(entity.getCustomProducts().get(0).getProduct().getName());
            if (entity.getCustomProducts().size() > 1) {
                sb.append(" 외 ").append(entity.getTotalQuantity()).append("건");
            }
            return sb.toString();
        }
    }
}
