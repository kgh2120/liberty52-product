package com.liberty52.product.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liberty52.product.service.applicationservice.impl.DeliveryOptionModifyServiceImpl;
import com.liberty52.product.service.controller.dto.DeliveryOptionDto;
import com.liberty52.product.service.controller.dto.DeliveryOptionFeeModify;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryOptionModifyController.class)
class DeliveryOptionModifyControllerTest {

    @MockBean
    private DeliveryOptionModifyServiceImpl deliveryOptionModifyService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("기본 배송비 수정")
    void updateDefaultDeliveryFee() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        given(deliveryOptionModifyService.updateDefaultDeliveryFeeByAdmin(anyString(), anyInt()))
                .willReturn(DeliveryOptionDto.builder()
                        .id(1L)
                        .fee(200_000)
                        .feeUpdatedAt(now)
                        .build());
        // when
        // then
        mvc.perform(patch("/admin/options/delivery/fee")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "ADMIN_ID")
                .header("LB-Role", "ADMIN")
                .content(objectMapper.writeValueAsString(
                        new DeliveryOptionFeeModify.Request(200_000)
                )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fee").value("200,000"))
                .andExpect(jsonPath("$.feeUpdatedAt").value(now.toString()));
    }

    @Test
    @DisplayName("기본 배송비 수정 - 배송비 최소값 불충족")
    void updatedDefaultDeliveryFee_whenFeeIsMinus() throws Exception {
        // given
        // when
        // then
        mvc.perform(patch("/admin/options/delivery/fee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "ADMIN_ID")
                        .header("LB-Role", "ADMIN")
                        .content(objectMapper.writeValueAsString(
                                new DeliveryOptionFeeModify.Request(-100_000)
                        )))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("기본 배송비 수정 - 배송비 최댓값 불충족")
    void updatedDefaultDeliveryFee_whenFeeIsOverHundred() throws Exception {
        // given
        // when
        // then
        mvc.perform(patch("/admin/options/delivery/fee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "ADMIN_ID")
                        .header("LB-Role", "ADMIN")
                        .content(objectMapper.writeValueAsString(
                                new DeliveryOptionFeeModify.Request(1_000_001)
                        )))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("기본 배송비 수정 - 배송비 Null")
    void updatedDefaultDeliveryFee_whenFeeIsNull() throws Exception {
        // given
        // when
        // then
        mvc.perform(patch("/admin/options/delivery/fee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "ADMIN_ID")
                        .header("LB-Role", "ADMIN"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}