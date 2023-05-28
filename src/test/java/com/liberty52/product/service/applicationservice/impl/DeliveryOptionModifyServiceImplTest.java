package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.service.controller.dto.DeliveryOptionDto;
import com.liberty52.product.service.repository.DeliveryOptionRepository;
import com.liberty52.product.service.utils.MockFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeliveryOptionModifyServiceImplTest {

    @Mock
    private DeliveryOptionRepository deliveryOptionRepository;

    @InjectMocks
    private DeliveryOptionModifyServiceImpl deliveryOptionModifyService;

    @Test
    @DisplayName("기본 배송비 수정")
    void updateDefaultDeliveryFee() throws Exception {
        // given
        given(deliveryOptionRepository.findById(anyLong()))
                .willReturn(Optional.of(MockFactory.mockDeliveryOptionOnlyFee(100_000)));
        // when
        DeliveryOptionDto dto = deliveryOptionModifyService.updateDefaultDeliveryFeeByAdmin("ADMIN", 200_000);
        // then
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(200_000, dto.getFee());
    }
}