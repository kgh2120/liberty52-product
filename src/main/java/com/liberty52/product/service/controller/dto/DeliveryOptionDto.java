package com.liberty52.product.service.controller.dto;

import com.liberty52.product.service.entity.DeliveryOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryOptionDto {
    private Long id;
    private int fee;
    private LocalDateTime feeUpdatedAt;

    public static DeliveryOptionDto fromEntity(DeliveryOption entity) {
        return DeliveryOptionDto.builder()
                .id(entity.getId())
                .fee(entity.getFee())
                .feeUpdatedAt(entity.getFeeUpdatedAt())
                .build();
    }
}
