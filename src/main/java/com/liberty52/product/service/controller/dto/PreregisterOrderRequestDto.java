package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PreregisterOrderRequestDto {

    private PaymentProductDto productDto;
    private DestinationDto destinationDto;
    private VBankDto vBankDto;

    public static PreregisterOrderRequestDto forTest(
            String productName, List<String> options, int quantity, List<String> orderOptions,
            String receiverName, String receiverEmail, String receiverPhoneNumber, String address1, String address2, String zipCode) {
        return new PreregisterOrderRequestDto(
                PaymentProductDto.forTest(productName, options, quantity, orderOptions),
                DestinationDto.create(receiverName, receiverEmail, receiverPhoneNumber, address1, address2, zipCode),
                null
        );
    }
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PaymentProductDto {
        private String productName;
        private List<String> options;
        private int quantity;
        private List<String> orderOptions;

        private static PaymentProductDto forTest(String pName, List<String> options, int quantity, List<String> orderOpts) {
            return new PaymentProductDto(pName, options, quantity, orderOpts);
        }

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

        public static PreregisterOrderRequestDto.DestinationDto create(String receiverName, String receiverEmail, String receiverPhoneNumber, String address1, String address2, String zipCode) {
            return new PreregisterOrderRequestDto.DestinationDto(receiverName, receiverEmail, receiverPhoneNumber, address1, address2, zipCode);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class VBankDto {
        private String vBank;
        private String vBankAccount;
        private String depositorBank;
        private String depositorName;
        private String depositorAccount;
    }


}
