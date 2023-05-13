package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.entity.OrderDestination;
import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.repository.OrdersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@SpringBootTest
@Transactional
class OrderDeleteServiceImplTest {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderDeleteService orderDeleteService;

    @Test
    void test_deleteOrderOfReadyByScheduled() throws Exception {
        Long N = 10L;
        for (int i = 0; i < N; i++) {
            preregisterCardPaymentOrders();
        }

        final OrderStatus ready = OrderStatus.READY;
        final LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        Long beforeCount = ordersRepository.countAllByOrderStatusAndOrderDateLessThan(ready, today);
        Assertions.assertEquals(N, beforeCount);

//        orderDeleteService.deleteOrderOfReadyByScheduled();
        ordersRepository.deleteAllByOrderStatusAndOrderDateLessThan(ready, today);

        Long afterCount = ordersRepository.countAllByOrderStatusAndOrderDateLessThan(ready, today);
        Assertions.assertEquals(0, afterCount);
    }

    void preregisterCardPaymentOrders() throws Exception {
        Orders order = Orders.create(
                UUID.randomUUID().toString(),
                OrderDestination.create("receiverName", "receiverEmail", "receiverPhoneNumber", "address1", "address2", "zipCode")
        );
        Field orderDate = order.getClass().getDeclaredField("orderDate");
        orderDate.setAccessible(true);
        orderDate.set(order, LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1));
        ordersRepository.save(order);
    }

}