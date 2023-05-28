package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class VBankModify {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank
        private String bank;
        @NotBlank
        private String accountNumber;
        @NotBlank
        private String holder;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String vBankId;
        private String bankOfVBank;
        private String accountNumber;
        private String holder;
        private String vBank;
        private String updatedAt;

        public static Response fromDto(VBankDto dto) {
            return Response.builder()
                    .vBankId(dto.getVBankId())
                    .bankOfVBank(dto.getBankOfVBank())
                    .accountNumber(dto.getAccountNumber())
                    .holder(dto.getHolder())
                    .vBank(dto.getVBank())
                    .updatedAt(dto.getUpdatedAt().toString())
                    .build();
        }
    }

}
