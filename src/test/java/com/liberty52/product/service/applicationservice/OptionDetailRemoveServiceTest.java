package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.OptionDetailRemoveRequestDto;
import com.liberty52.product.service.entity.OptionDetail;
import com.liberty52.product.service.repository.OptionDetailRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.liberty52.product.global.contants.RoleConstants.ADMIN;

@SpringBootTest
@Transactional
public class OptionDetailRemoveServiceTest {

    @Autowired
    OptionDetailRemoveService optionDetailRemoveService;

    @Autowired
    OptionDetailRepository optionDetailRepository;

    @Test
    void 옵션선택지삭제(){
        String detailId = "OPT-002";
        OptionDetailRemoveRequestDto dto = OptionDetailRemoveRequestDto.create(false);
        OptionDetail beforeOptionDetail = optionDetailRepository.findById(detailId).orElseGet(null);
        boolean onSail = beforeOptionDetail.isOnSale();
        optionDetailRemoveService.removeOptionDetail(ADMIN, detailId, dto);
        OptionDetail afterOptionDetail = optionDetailRepository.findById(detailId).orElseGet(null);
        Assertions.assertEquals(afterOptionDetail.isOnSale(), !onSail);

    }
}
