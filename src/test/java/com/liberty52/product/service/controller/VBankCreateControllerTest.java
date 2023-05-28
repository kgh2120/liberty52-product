package com.liberty52.product.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liberty52.product.service.applicationservice.impl.VBankCreateServiceImpl;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VBankCreateController.class)
class VBankCreateControllerTest {

    @MockBean
    private VBankCreateServiceImpl vBankCreateService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("controller-가상계좌 추가")
    void successCreateVBank() throws Exception {
        // given
        given(vBankCreateService.createVBankByAdmin(anyString(), anyString(), anyString(), anyString()))
                .willReturn(VBankDto.builder()
                        .vBankId("id")
                        .bankOfVBank("하나은행")
                        .accountNumber("test_account")
                        .holder("test_holder")
                        .vBank("하나은행 test_account test_holder")
                        .createdAt(LocalDateTime.now())
                        .build()
                );
        // when
        // then
        mockMvc.perform(post("/admin/vbanks")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "ADMIN_ID")
                .header("LB-Role", "ADMIN")
                .content(objectMapper.writeValueAsString(
                        VBankCreate.Request.builder()
                                .bank("하나은행")
                                .accountNumber("test_account")
                                .holder("test_holder")
                                .build()
                )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vbankId").value("id"))
                .andExpect(jsonPath("$.bankOfVBank").value("하나은행"))
                .andExpect(jsonPath("$.accountNumber").value("test_account"))
                .andExpect(jsonPath("$.holder").value("test_holder"))
                .andExpect(jsonPath("$.vbank").value("하나은행 test_account test_holder"))
                .andDo(print());

    }
    
}