package com.liberty52.product.service.applicationservice;

import static com.liberty52.product.global.constants.RoleConstants.ADMIN;

import com.liberty52.product.global.exception.external.badrequest.AbnormalOrderStatusRequestException;
import com.liberty52.product.global.exception.external.badrequest.OrderStatusIsNotWaitingDepositException;
import com.liberty52.product.global.exception.external.badrequest.SameOrderStatusRequestException;
import com.liberty52.product.service.controller.dto.VBankStatusModifyDto;
import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.repository.OrdersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderStatusModifyServiceImplTest {
  @Autowired
  private OrderStatusModifyService orderStatusModifyService;
  @Autowired
  private OrdersRepository ordersRepository;

  private final String orderId = "GORDER-001";
  private final String waiting_deposit_orderId = "GORDER-0011";

  @Test
  void modifyOrderStatusByAdmin(){
    Orders order = ordersRepository.findById(orderId).get();
    orderStatusModifyService.modifyOrderStatusByAdmin(ADMIN,order.getId(),OrderStatus.MAKING);
    Assertions.assertEquals(order.getOrderStatus(),OrderStatus.MAKING);
  }
  @Test
  void validateOrderStatus() {
    Assertions.assertThrows(SameOrderStatusRequestException.class,()->
        orderStatusModifyService.modifyOrderStatusByAdmin(ADMIN, orderId, OrderStatus.ORDERED));
    Assertions.assertThrows(AbnormalOrderStatusRequestException.class,()->
        orderStatusModifyService.modifyOrderStatusByAdmin(ADMIN, orderId, OrderStatus.COMPLETE));
  }

  @Test
  void modifyOrderStatusOfVBankByAdmin() {
    Orders order = ordersRepository.findById(waiting_deposit_orderId).get();
    VBankStatusModifyDto dto = new VBankStatusModifyDto("우리은행","박찬길","334-442-1235");
    orderStatusModifyService.modifyOrderStatusOfVBankByAdmin(ADMIN,order.getId(),dto);
    Assertions.assertEquals(order.getOrderStatus(),OrderStatus.ORDERED);
  }
  @Test
  void modifyOrderStatusOfVBankByAdmin_State_IsNot_Waiting_Deposit() {
    Orders order = ordersRepository.findById(orderId).get();
    VBankStatusModifyDto dto = new VBankStatusModifyDto("우리은행","박찬길","334-442-1235");
    Assertions.assertThrows(OrderStatusIsNotWaitingDepositException.class,()->
        orderStatusModifyService.modifyOrderStatusOfVBankByAdmin(ADMIN,order.getId(),dto));
  }
}
