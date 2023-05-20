package com.liberty52.product.service.entity.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liberty52.product.service.controller.dto.OrderCancelDto;
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

        // Refund
        private String refundBank;
        private String refundHolder;
        private String refundAccount;
        private String refundPhoneNum;

        private VBankPaymentInfo(String vBankInfo, String depositorBank, String depositorName, String depositorAccount, Boolean isApplyCashReceipt) {
            this.vbankInfo = vBankInfo;
            this.depositorBank = depositorBank;
            this.depositorName = depositorName;
            this.depositorAccount = depositorAccount;
            this.isApplyCashReceipt = isApplyCashReceipt;
            this.paidAt = LocalDateTime.now();
        }

        private VBankPaymentInfo(VBankPaymentInfo prev) {
            this.vbankInfo = prev.vbankInfo;
            this.depositorBank = prev.depositorBank;
            this.depositorName = prev.depositorName;
            this.depositorAccount = prev.depositorAccount;
            this.isApplyCashReceipt = prev.isApplyCashReceipt;
            this.paidAt = prev.paidAt;
            this.refundBank = prev.refundBank;
            this.refundHolder = prev.refundHolder;
            this.refundAccount = prev.refundAccount;
            this.refundPhoneNum = prev.refundPhoneNum;
        }

        public static VBankPaymentInfo of(String vBankInfo, String depositorBank, String depositorName, String depositorAccount, Boolean isApplyCashReceipt) {
            return new VBankPaymentInfo(vBankInfo, depositorBank, depositorName, depositorAccount, isApplyCashReceipt);
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

        public static VBankPaymentInfo refund(VBankPaymentInfo prev, OrderCancelDto.Request.RefundVO refundVO) {
            VBankPaymentInfo newInstance = new VBankPaymentInfo(prev);
            newInstance.refundBank = refundVO.getRefundBank();
            newInstance.refundHolder = refundVO.getRefundHolder();
            newInstance.refundAccount = refundVO.getRefundAccount();
            newInstance.refundPhoneNum = refundVO.getRefundPhoneNum();
            return newInstance;
        }

    }
}
