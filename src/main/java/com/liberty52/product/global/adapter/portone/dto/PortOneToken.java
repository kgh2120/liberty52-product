package com.liberty52.product.global.adapter.portone.dto;

import com.liberty52.product.global.util.Utils;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PortOneToken {

    private String accessToken;
    private LocalDateTime now;
    private LocalDateTime expiredAt;

    public static PortOneToken of(PortOneTokenResponseDto.PortOneTokenResponse dto) {
        PortOneToken portOneToken = new PortOneToken();
        portOneToken.accessToken = dto.getAccess_token();
        portOneToken.now = Utils.convertUnixToLocalDateTime(dto.getNow());
        portOneToken.expiredAt = Utils.convertUnixToLocalDateTime(dto.getExpired_at());
        return portOneToken;
    }


}