package com.liberty52.product.global.adapter.kafka;


import com.liberty52.product.service.applicationservice.ReviewRemoveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaConsumerImpl implements
        KafkaConsumer {

    private final ReviewRemoveService reviewRemoveService;

    @KafkaListener(topics = "member-deleted", groupId = "liberty52")
    @Override
    public void consumeMemberDeletedEvent(String authId) {
        reviewRemoveService.removeAllReview(authId);

    }
}
