package com.liberty52.product.global.adapter.kafka;

public interface KafkaConsumer {

    void consumeMemberDeletedEvent(String authId);

}
