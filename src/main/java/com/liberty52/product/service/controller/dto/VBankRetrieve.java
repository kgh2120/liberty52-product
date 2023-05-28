package com.liberty52.product.service.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class VBankRetrieve {

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
        private String createdAt;
        private String updatedAt;

        public static Response fromDto(VBankDto dto) {
            return Response.builder()
                    .vBankId(dto.getVBankId())
                    .bankOfVBank(dto.getBankOfVBank())
                    .accountNumber(dto.getAccountNumber())
                    .holder(dto.getHolder())
                    .vBank(dto.getVBank())
                    .createdAt(dto.getCreatedAt().toString())
                    .updatedAt(
                            dto.getUpdatedAt() != null ?
                                    dto.getUpdatedAt().toString() : null
                    )
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListResponse {
        private List<Response> vbanks;

        public static ListResponse fromDtoList(List<VBankDto> dtos) {
            return ListResponse.builder()
                    .vbanks(dtos.stream()
                            .map(Response::fromDto)
                            .toList())
                    .build();
        }
    }

}
