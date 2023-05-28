package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.badrequest.BadRequestException;
import com.liberty52.product.global.exception.external.forbidden.InvalidRoleException;
import com.liberty52.product.service.controller.dto.VBankDto;
import com.liberty52.product.service.entity.payment.BankType;
import com.liberty52.product.service.entity.payment.VBank;
import com.liberty52.product.service.repository.VBankRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VBankCreateServiceImplTest {

    @Mock
    private VBankRepository vBankRepository;

    @InjectMocks
    private VBankCreateServiceImpl vBankCreateService;
    
    @Test
    @DisplayName("가상계좌 추가")
    void createVBank() {
        // given
        VBank vbank = VBank.of(BankType.HANA, "test_account", "test_holder");
        given(vBankRepository.save(any()))
                .willReturn(vbank);
        ArgumentCaptor<VBank> captor = ArgumentCaptor.forClass(VBank.class);

        // when
        VBankDto vBankDto = vBankCreateService.createVBankByAdmin("ADMIN", "하나은행", "account", "holder");
        
        // then
        verify(vBankRepository, times(1)).save(captor.capture());
        assertNotNull(vBankDto);
        assertEquals(BankType.HANA.getKoName(), vBankDto.getBankOfVBank());
        assertEquals("test_account", vBankDto.getAccountNumber());
        assertEquals("test_holder", vBankDto.getHolder());
        assertEquals(BankType.HANA, captor.getValue().getBank());
        assertEquals("account", captor.getValue().getAccount());
        assertEquals("holder", captor.getValue().getHolder());
    }

    @Test
    @DisplayName("가상계좌 추가 실패 - 관리자 아닌 유저")
    void createVBank_throw_InvalidRole() {
        // given
        // when
        // then
        assertThrows(
                InvalidRoleException.class,
                () -> vBankCreateService.createVBankByAdmin("NOT_ADMIN", "hana", "acc", "holder")
        );
    }
    
    @Test
    @DisplayName("가상계좌 추가 실패 - 유효하지 않은 은행")
    void createVBank_throw_bank_badRequest() {
        // given
        // when
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> vBankCreateService.createVBankByAdmin("ADMIN", "wrong", "acc", "holder")
        );
        // then
        assertEquals("유효하지 않은 은행 종류입니다. 요청은행:wrong", exception.getErrorMessage());
    }

    @Test
    @DisplayName("가상계좌 추가 실패 - 이미 존재하는 가상계좌")
    void createVBank_throw_alreadyExistVBank() {
        // given
        given(vBankRepository.existsByBankAndAccountAndHolder(any(), anyString(), anyString()))
                .willReturn(true);
        // when
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> vBankCreateService.createVBankByAdmin("ADMIN", "하나은행", "acc", "holder")
        );
        // then
        assertEquals("이미 등록되어 있는 가상계좌입니다.", exception.getErrorMessage());
    }


}