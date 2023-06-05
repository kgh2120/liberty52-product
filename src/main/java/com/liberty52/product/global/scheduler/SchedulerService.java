package com.liberty52.product.global.scheduler;

import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.repository.CustomProductRepository;
import com.liberty52.product.service.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final OrdersRepository ordersRepository;
    private final CustomProductRepository customProductRepository;

    /** 매일 23시 55분에 어제의 Ready 상태인 Order 및 Custom Product 삭제 */
    @Async
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Scheduled(cron = "0 55 23 * * *", zone = "Asia/Seoul")
    public void deleteOrderOfReadyByScheduled() {
        OrderStatus ready = OrderStatus.READY;
        LocalDateTime yesterday = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(1L);

        List<Orders> orders = ordersRepository.findAllByOrderStatusAndOrderedAtLessThan(ready, yesterday);
        log.info("ORDER SCHEDULED: Now, There are {} Ready status Order of yesterday", orders.size());

        if (CollectionUtils.isEmpty(orders)) return;

        orders.forEach(e -> customProductRepository.deleteAll(e.getCustomProducts()));
        ordersRepository.deleteAll(orders);
        log.info("ORDER SCHEDULED: Now, Delete {} Ready status Order of yesterday", orders.size());
    }

}
