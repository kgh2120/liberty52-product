package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.OrderStatusModifyService;
import com.liberty52.product.service.controller.dto.VBankStatusModifyDto;
import com.liberty52.product.service.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderStatusModifyController {

  private final OrderStatusModifyService orderStatusModifyService;

  @PutMapping("/admin/orders/{orderId}/status")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void modifyOrderStatusByAdmin(
      @RequestHeader("LB-Role") String role,
      @PathVariable String orderId, @RequestParam OrderStatus orderStatus
  ) {
    orderStatusModifyService.modifyOrderStatusByAdmin(role, orderId, orderStatus);
  }

  @PutMapping("/admin/orders/{orderId}/vbank")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void modifyOrderStatusOfVBank(
      @RequestHeader("LB-Role") String role, @PathVariable String orderId,
      @Validated @RequestBody VBankStatusModifyDto dto
  ) {
    orderStatusModifyService.modifyOrderStatusOfVBank(role, orderId,dto);
  }
}
