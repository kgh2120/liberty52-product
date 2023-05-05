package com.liberty52.product.service.entity.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liberty52.product.service.controller.dto.OrderCreateRequestDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue(value = "VBANK")
public class VBankPayment extends Payment<VBankPayment.VBankPaymentInfo> {



    public VBankPayment() {
        super();
        this.type = PaymentType.VBANK;
    }

    public static VBankPayment of() {
        return new VBankPayment();
    }

    @Override
    public <T extends PaymentInfo> void setInfo(T dto) {
        try {
            if (!(dto instanceof VBankPayment.VBankPaymentInfo)) {
                return;
            }
            this.info = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public VBankPayment.VBankPaymentInfo getInfoAsDto() {
        try {
            return objectMapper.readValue(this.info, VBankPaymentInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getInfoAsString() {
        return this.info;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class VBankPaymentInfo extends PaymentInfo {
        private String vbankInfo;
        private String depositorBank;
        private String depositorName;
        private String depositorAccount;
        private Boolean isApplyCashReceipt;
        private LocalDateTime paidAt;

        public static VBankPaymentInfo of(String vBankInfo, String depositorBank, String depositorName, String depositorAccount, Boolean isApplyCashReceipt) {
            return new VBankPaymentInfo(vBankInfo, depositorBank, depositorName, depositorAccount, isApplyCashReceipt, LocalDateTime.now());
        }

        public static VBankPaymentInfo ofWaitingDeposit(OrderCreateRequestDto.VbankDto dto) {
            VBankPaymentInfo info = new VBankPaymentInfo();
            info.vbankInfo = dto.getVbankInfo();
            info.depositorName = dto.getDepositorName();
            info.isApplyCashReceipt = dto.getIsApplyCashReceipt();
            return info;
        }

        public static VBankPaymentInfo ofPaid() {
            return null;
        }
    }
}
