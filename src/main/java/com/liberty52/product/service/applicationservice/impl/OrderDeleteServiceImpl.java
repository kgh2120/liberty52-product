package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.service.applicationservice.OrderDeleteService;
import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderDeleteServiceImpl implements OrderDeleteService {

    private final OrdersRepository ordersRepository;

    /** 매일 23시 50분부터 59분까지 5분 간격으로 어제의 Ready 상태인 Order 삭제 */
    @Override
    @Async
    @Scheduled(cron = "0 50/5 23 * * *", zone = "Asia/Seoul")
    public void deleteOrderOfReadyByScheduled() {
        OrderStatus ready = OrderStatus.READY;
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        Long count = ordersRepository.countAllByOrderStatusAndOrderDateLessThan(ready, today);
        log.info("ORDER SCHEDULED: Now, There are {} Ready status Order of yesterday", count);

        ordersRepository.deleteAllByOrderStatusAndOrderDateLessThan(ready, today);
        log.info("ORDER SCHEDULED: Now, Delete {} Ready status Order of yesterday", count);
    }
}
