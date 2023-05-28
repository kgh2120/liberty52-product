package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.badrequest.BadRequestException;
import com.liberty52.product.global.exception.external.forbidden.InvalidRoleException;
import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.service.entity.payment.VBank;
import com.liberty52.product.service.repository.VBankRepository;
import com.liberty52.product.service.utils.MockFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VBankDeleteServiceImplTest {

    @Mock
    private VBankRepository vBankRepository;

    @InjectMocks
    private VBankDeleteServiceImpl vBankDeleteService;

    @Test
    @DisplayName("가상계좌 삭제")
    void deleteVBank() throws Exception {
        // given
        given(vBankRepository.findById(anyString()))
                .willReturn(Optional.of(MockFactory.mockVBank()));
        given(vBankRepository.countAll())
                .willReturn(5L);
        ArgumentCaptor<VBank> captor = ArgumentCaptor.forClass(VBank.class);
        // when
        vBankDeleteService.deleteVBankByAdmin("ADMIN", "m_vBankId");
        // then
        verify(vBankRepository, times(1)).delete(captor.capture());
        Assertions.assertEquals("m_vBankId", captor.getValue().getId());
    }

    @Test
    @DisplayName("가상계좌 삭제 실패 - 관리자 아닌 유저")
    void deleteVBank_throw_InvalidRole() {
        // given
        // when
        InvalidRoleException exception = Assertions.assertThrows(
                InvalidRoleException.class,
                () -> vBankDeleteService.deleteVBankByAdmin("NOT_ADMIN", "")
        );
        // then
        Assertions.assertTrue(exception.getErrorMessage().contains("NOT_ADMIN"));
    }

    @Test
    @DisplayName("가상계좌 삭제 실패 - 리소스 없음")
    void deleteVBank_throw_ResourceNotFound() {
        // given
        given(vBankRepository.findById(anyString()))
                .willReturn(Optional.empty());
        // when
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> vBankDeleteService.deleteVBankByAdmin("ADMIN", "")
        );
        // then
        Assertions.assertTrue(exception.getErrorMessage().contains("VBANK"));
        Assertions.assertTrue(exception.getErrorMessage().contains("id"));
    }

    @Test
    @DisplayName("가상계좌 삭제 실패 - 반드시 1개 이상 유지")
    void deleteVBank_throw_HaveToExistAtLeastOne() throws Exception {
        // given
        given(vBankRepository.findById(anyString()))
                .willReturn(Optional.of(MockFactory.mockVBank()));
        given(vBankRepository.countAll())
                .willReturn(1L);
        // when
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> vBankDeleteService.deleteVBankByAdmin("ADMIN", "m_vBankId")
        );
        // then
        Assertions.assertTrue(exception.getErrorMessage().contains("가상계좌는 반드시 1개 이상 존재해야 합니다."));
    }

}