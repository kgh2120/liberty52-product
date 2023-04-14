package com.liberty52.product.service.controller.dto;

import com.liberty52.product.service.entity.OrderStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MonoItemOrderResponseDto {
    private String id;
    private LocalDate orderDate;
    private String status;



    public static MonoItemOrderResponseDto create(String id, LocalDate orderDate, OrderStatus orderStatus) {
        return builder().id(id).orderDate(orderDate).status(orderStatus.name()).build();
    }
}
