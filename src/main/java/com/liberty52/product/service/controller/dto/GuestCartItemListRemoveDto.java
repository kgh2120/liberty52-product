package com.liberty52.product.service.controller.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GuestCartItemListRemoveDto {
    private String guestId;
    private List<String> ids;
}
