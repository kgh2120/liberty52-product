package com.liberty52.product.service.controller.dto;

import com.liberty52.product.service.entity.Orders;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminCanceledOrderDetailResponse {

    private OrderDetailRetrieveResponse basicOrderDetail;
    private CanceledOrderDetailResponse canceledInfo;

    public static AdminCanceledOrderDetailResponse of(Orders order, String customerName) {
        return new AdminCanceledOrderDetailResponse(
                OrderDetailRetrieveResponse.of(order, customerName),
                CanceledOrderDetailResponse.of(order)
        );
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CanceledOrderDetailResponse {
        private String reason;
        private String reqAt;
        private String canceledAt;
        private Integer fee;
        private String approvedAdminName;

        public static CanceledOrderDetailResponse of(Orders order) {
            CanceledOrderDetailResponse response = new CanceledOrderDetailResponse();
            response.reason = order.getCanceledOrders().getReason();
            response.reqAt = order.getCanceledOrders().getReqAt().toString();
            response.canceledAt = order.getCanceledOrders().getCanceledAt() != null ?
                    order.getCanceledOrders().getCanceledAt().toString() : "대기중";
            response.fee = order.getCanceledOrders().getFee();
            response.approvedAdminName = order.getCanceledOrders().getApprovedAdminName();
            return response;
        }
    }
}
