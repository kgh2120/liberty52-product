package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.VBankStatusModifyDto;
import com.liberty52.product.service.entity.OrderStatus;

public interface OrderStatusModifyService {

  void modifyOrderStatusByAdmin(String role, String orderId, OrderStatus status);

  void modifyOrderStatusOfVBank(String role,String orderId, VBankStatusModifyDto dto);
}
