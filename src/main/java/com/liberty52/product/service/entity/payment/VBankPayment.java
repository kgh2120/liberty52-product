package com.liberty52.product.service.entity.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.liberty52.product.service.controller.dto.PreregisterOrderRequestDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue(value = "VBANK")
public class VBankPayment extends Payment<VBankPayment.VBankPaymentInfo> {

    //TODO bean 으로 생성할 것
    private static final ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

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
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class VBankPaymentInfo extends PaymentInfo {
        private String vBank;
        private String vBankAccount;
        private String depositorBank;
        private String depositorName;
        private String depositorAccount;
        private LocalDateTime paidAt;

        public static VBankPaymentInfo of(String vBank, String vBankAccount, String depositorBank, String depositorName, String depositorAccount) {
            return new VBankPaymentInfo(vBank, vBankAccount, depositorBank, depositorName, depositorAccount, LocalDateTime.now());
        }

        public static VBankPaymentInfo of(PreregisterOrderRequestDto.VBankDto dto) {
            return new VBankPaymentInfo(
                    dto.getVBank(), dto.getVBankAccount(), dto.getDepositorBank(), dto.getDepositorName(), dto.getDepositorAccount(), LocalDateTime.now()
            );
        }
    }
}
