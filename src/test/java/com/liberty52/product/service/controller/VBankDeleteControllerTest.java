package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.impl.VBankDeleteServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VBankDeleteController.class)
class VBankDeleteControllerTest {

    @MockBean
    private VBankDeleteServiceImpl vBankDeleteService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("가상계좌 삭제")
    void deleteVBank() throws Exception {
        // given
        // when
        // then
        mvc.perform(delete("/admin/vbanks/vbankId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "ADMIN_ID")
                .header("LB-Role", "ADMIN"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}