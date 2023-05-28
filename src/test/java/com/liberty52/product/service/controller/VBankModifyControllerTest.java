package com.liberty52.product.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liberty52.product.service.applicationservice.impl.VBankModifyServiceImpl;
import com.liberty52.product.service.controller.dto.VBankCreate;
import com.liberty52.product.service.controller.dto.VBankDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VBankModifyController.class)
class VBankModifyControllerTest {

    @MockBean
    private VBankModifyServiceImpl vBankModifyService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("가상계좌 수정")
    void updateVBank() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        // given
        given(vBankModifyService.updateVBankByAdmin(anyString(), anyString(), anyString(), anyString(), anyString()))
                .willReturn(VBankDto.builder()
                        .vBankId("id")
                        .bankOfVBank("국민은행")
                        .accountNumber("u_account")
                        .holder("u_holder")
                        .vBank("하나은행 u_account u_holder")
                        .createdAt(now)
                        .updatedAt(now)
                        .build());
        // when
        // then
        mockMvc.perform(put("/admin/vbanks/id")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "ADMIN_ID")
                .header("LB-Role", "ADMIN")
                .content(objectMapper.writeValueAsString(
                        VBankCreate.Request.builder()
                                .bank("국민은행")
                                .accountNumber("u_account")
                                .holder("u_holder")
                                .build()
                ))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vbankId").value("id"))
                .andExpect(jsonPath("$.bankOfVBank").value("국민은행"))
                .andExpect(jsonPath("$.accountNumber").value("u_account"))
                .andExpect(jsonPath("$.holder").value("u_holder"))
                .andExpect(jsonPath("$.vbank").value("하나은행 u_account u_holder"))
                .andExpect(jsonPath("$.updatedAt").value(now.toString()));
    }

}