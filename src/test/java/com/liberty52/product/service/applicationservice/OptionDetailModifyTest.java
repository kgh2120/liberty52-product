package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.OptionDetailModifyRequestDto;
import com.liberty52.product.service.controller.dto.OptionDetailOnSailModifyRequestDto;
import com.liberty52.product.service.entity.OptionDetail;
import com.liberty52.product.service.repository.OptionDetailRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.liberty52.product.global.constants.RoleConstants.ADMIN;

@SpringBootTest
@Transactional
public class OptionDetailModifyTest {

    @Autowired
    OptionDetailModifyService optionDetailModifyService;

    @Autowired
    OptionDetailRepository optionDetailRepository;

    @Test
    void 옵션선택지삭제상태(){
        String detailId = "OPT-002";
        OptionDetail beforeOptionDetail = optionDetailRepository.findById(detailId).orElse(null);
        boolean onSail = beforeOptionDetail.isOnSale();

        OptionDetailOnSailModifyRequestDto dto = OptionDetailOnSailModifyRequestDto.create(!onSail);

        optionDetailModifyService.modifyOptionDetailOnSailStateByAdmin(ADMIN, detailId, dto);
        OptionDetail afterOptionDetail = optionDetailRepository.findById(detailId).orElse(null);
        Assertions.assertEquals(afterOptionDetail.isOnSale(), !onSail);

    }

    @Test
    void 옵션선택지수정(){
        String detailId = "OPT-002";
        String name = "테스트";
        int price = 3;
        OptionDetail beforeOptionDetail = optionDetailRepository.findById(detailId).orElse(null);
        boolean onSail = beforeOptionDetail.isOnSale();

        OptionDetailModifyRequestDto dto = OptionDetailModifyRequestDto.create(name,price,!onSail);
        optionDetailModifyService.modifyOptionDetailByAdmin(ADMIN, detailId, dto);
        OptionDetail afterOptionDetail = optionDetailRepository.findById(detailId).orElse(null);
        Assertions.assertEquals(afterOptionDetail.isOnSale(), !onSail);
        Assertions.assertEquals(afterOptionDetail.getName(), name);
        Assertions.assertEquals(afterOptionDetail.getPrice(), price);
    }
}
