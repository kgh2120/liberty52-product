package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.badrequest.BadRequestException;
import com.liberty52.product.global.exception.external.forbidden.InvalidRoleException;
import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.service.controller.dto.VBankDto;
import com.liberty52.product.service.repository.VBankRepository;
import com.liberty52.product.service.utils.MockFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VBankModifyServiceImplTest {

    @Mock
    private VBankRepository vBankRepository;

    @InjectMocks
    private VBankModifyServiceImpl vBankModifyService;

    @Test
    @DisplayName("가상계좌 수정")
    void updateVBank() throws Exception {
        // given
        given(vBankRepository.findById(anyString()))
                .willReturn(Optional.of(MockFactory.mockVBank()));
        // when
        VBankDto vBankDto = vBankModifyService.updateVBankByAdmin("ADMIN", "m_vBankId", "국민은행",
                "u_account", "u_holder");
        // then
        Assertions.assertNotNull(vBankDto);
        Assertions.assertEquals("m_vBankId", vBankDto.getVBankId());
        Assertions.assertNotEquals("하나은행", vBankDto.getBankOfVBank());
        Assertions.assertNotEquals("m_account", vBankDto.getAccountNumber());
        Assertions.assertNotEquals("m_holder", vBankDto.getHolder());
        Assertions.assertEquals("국민은행", vBankDto.getBankOfVBank());
        Assertions.assertEquals("u_account", vBankDto.getAccountNumber());
        Assertions.assertEquals("u_holder", vBankDto.getHolder());
        Assertions.assertNotNull(vBankDto.getUpdatedAt());
    }

    @Test
    @DisplayName("가상계좌 수정 실패 - 관리자 아닌 유저")
    void updateVBank_throw_InvalidRole() {
        // given
        // when
        // then
        Assertions.assertThrows(
                InvalidRoleException.class,
                () -> vBankModifyService.updateVBankByAdmin("NOT_ADMIN","","","","")
        );
    }

    @Test
    @DisplayName("가상계좌 수정 실패 - 리소스 없음")
    void updateVBank_throw_ResourceNotFound() {
        // given
        given(vBankRepository.findById(anyString()))
                .willReturn(Optional.empty());
        // when
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> vBankModifyService.updateVBankByAdmin("ADMIN", "", "", "", "")
        );
        // then
        Assertions.assertTrue(exception.getErrorMessage().contains("VBANK"));
        Assertions.assertTrue(exception.getErrorMessage().contains("id"));
    }

    @Test
    @DisplayName("가상계좌 수정 실패 - 유효하지 않은 은행")
    void updateVBank_throw_Bank_BadRequest() throws Exception {
        // given
        given(vBankRepository.findById(anyString()))
                .willReturn(Optional.of(MockFactory.mockVBank()));
        // when
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> vBankModifyService.updateVBankByAdmin("ADMIN", "m_vBankId", "wrong", "", "")
        );
        // then
        Assertions.assertTrue(exception.getErrorMessage().contains("유효하지 않은 은행 종류입니다."));
    }


}