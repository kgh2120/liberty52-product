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
public class OrderCreateRequestDto {

    private PaymentProductDto productDto;
    private DestinationDto destinationDto;
    private VbankDto vbankDto;
    /** For Cart orders */
    private List<String> customProductIdList;

    public static OrderCreateRequestDto forTestCard(
            String productName, List<String> options, int quantity, List<String> orderOptions,
            String receiverName, String receiverEmail, String receiverPhoneNumber, String address1, String address2, String zipCode) {
        return new OrderCreateRequestDto(
                PaymentProductDto.forTest(productName, options, quantity, orderOptions),
                DestinationDto.create(receiverName, receiverEmail, receiverPhoneNumber, address1, address2, zipCode),
                null, null
        );
    }
    public static OrderCreateRequestDto forTestVBank(
            String productName, List<String> options, int quantity, List<String> orderOptions,
            String receiverName, String receiverEmail, String receiverPhoneNumber, String address1, String address2, String zipCode,
            String vBankInfo, String depositorName) {
        return new OrderCreateRequestDto(
                PaymentProductDto.forTest(productName, options, quantity, orderOptions),
                DestinationDto.create(receiverName, receiverEmail, receiverPhoneNumber, address1, address2, zipCode),
                VbankDto.forTest(vBankInfo, depositorName, false),
                null
        );
    }

    public static OrderCreateRequestDto forTestCardByCarts(
            List<String> customProductIdList,
            String receiverName, String receiverEmail, String receiverPhoneNumber, String address1, String address2, String zipCode) {
        return new OrderCreateRequestDto(
                null,
                DestinationDto.create(receiverName, receiverEmail, receiverPhoneNumber, address1, address2, zipCode),
                null,
                customProductIdList
        );
    }

    public static OrderCreateRequestDto forTestVBankByCarts(
            List<String> customProductIdList,
            String receiverName, String receiverEmail, String receiverPhoneNumber, String address1, String address2, String zipCode,
            String vBankInfo, String depositorName) {
        return new OrderCreateRequestDto(
                null,
                DestinationDto.create(receiverName, receiverEmail, receiverPhoneNumber, address1, address2, zipCode),
                VbankDto.forTest(vBankInfo, depositorName, false),
                customProductIdList
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
        @NotBlank
        private String receiverPhoneNumber;
        @NotBlank
        private String address1;
        @NotBlank
        private String address2;
        @NotBlank
        private String zipCode;

        public static OrderCreateRequestDto.DestinationDto create(String receiverName, String receiverEmail, String receiverPhoneNumber, String address1, String address2, String zipCode) {
            return new OrderCreateRequestDto.DestinationDto(receiverName, receiverEmail, receiverPhoneNumber, address1, address2, zipCode);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class VbankDto {
        @NotBlank
        private String vbankInfo;
        @NotBlank
        private String depositorName;
        private Boolean isApplyCashReceipt;

        public static VbankDto forTest(String vBankInfo, String depositorName, Boolean isApplyCashReceipt) {
            return new VbankDto(vBankInfo, depositorName, isApplyCashReceipt);
        }
    }


}
