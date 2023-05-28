package com.liberty52.product.service.controller.dto;

import com.liberty52.product.service.entity.payment.VBank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VBankDto {
    private String vBankId;
    private String bankOfVBank;
    private String accountNumber;
    private String holder;
    private String vBank;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static VBankDto fromEntity(VBank vBank) {
        return VBankDto.builder()
                .vBankId(vBank.getId())
                .bankOfVBank(vBank.getBank().getKoName())
                .accountNumber(vBank.getAccount())
                .holder(vBank.getHolder())
                .vBank(vBank.getOneLineVBankInfo())
                .createdAt(vBank.getCreatedAt())
                .updatedAt(vBank.getUpdatedAt())
                .build();
    }
}