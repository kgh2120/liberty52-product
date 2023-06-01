package com.liberty52.product.service.controller.dto;

import com.liberty52.product.service.entity.OrderStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MonoItemOrderResponseDto {
    private String id;
    private LocalDate orderDate;
    private String status;



    public static MonoItemOrderResponseDto create(String id, LocalDateTime orderedAt, OrderStatus orderStatus) {
        return builder().id(id).orderDate(orderedAt.toLocalDate()).status(orderStatus.name()).build();
    }
}
