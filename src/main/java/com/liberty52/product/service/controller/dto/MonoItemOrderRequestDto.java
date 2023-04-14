package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MonoItemOrderRequestDto {
    @NotBlank
    private String productName;
    @NotNull
    private List<String> options;
    @Min(1)
    private int quantity;
    @Min(0)
    private int deliveryPrice;
    @NotNull
    private MonoItemOrderRequestDto.DestinationDto destination;

    public static MonoItemOrderRequestDto createForTest(String productName, List<String> details, int quantity, int deliveryPrice, DestinationDto destinationDto) {
        return new MonoItemOrderRequestDto(productName, details, quantity, deliveryPrice, destinationDto);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DestinationDto {
        @NotBlank
        private String receiverName;
        @NotBlank
        private String receiverEmail;
        private String receiverPhoneNumber;
        @NotBlank
        private String address1;
        @NotBlank
        private String address2;
        @NotBlank
        private String zipCode;

        public static DestinationDto create(String receiverName, String receiverEmail, String receiverPhoneNumber, String address1, String address2, String zipCode) {
            return new DestinationDto(receiverName, receiverEmail, receiverPhoneNumber, address1, address2, zipCode);
        }
    }
}
