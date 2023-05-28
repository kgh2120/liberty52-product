package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.service.controller.dto.DeliveryOptionDto;
import com.liberty52.product.service.entity.DeliveryOption;
import com.liberty52.product.service.repository.DeliveryOptionRepository;
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
class DeliveryOptionRetrieveServiceImplTest {

    @Mock
    private DeliveryOptionRepository deliveryOptionRepository;

    @InjectMocks
    private DeliveryOptionRetrieveServiceImpl deliveryOptionRetrieveService;

    @Test
    @DisplayName("기본 배송비 조회")
    void getDefaultDeliveryFee() {
        // given
        given(deliveryOptionRepository.findById(anyLong()))
                .willReturn(Optional.of(DeliveryOption.feeOf(100000)));
        // when
        DeliveryOptionDto dto = deliveryOptionRetrieveService.getDefaultDeliveryFee();
        // then
        Assertions.assertEquals(100000, dto.getFee());
    }

}