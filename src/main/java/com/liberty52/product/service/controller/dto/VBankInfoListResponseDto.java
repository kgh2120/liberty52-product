package com.liberty52.product.service.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VBankInfoListResponseDto {

    private List<VBankInfoDto> vbankInfos;

    public static VBankInfoListResponseDto of(List<VBankInfoDto> vbankInfos) {
        return new VBankInfoListResponseDto(vbankInfos);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class VBankInfoDto {
        private String account;

        public static VBankInfoDto of(String account) {
            return new VBankInfoDto(account);
        }
    }

}
