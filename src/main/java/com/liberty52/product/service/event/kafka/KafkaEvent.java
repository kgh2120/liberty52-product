package com.liberty52.product.service.event.kafka;

import com.liberty52.product.service.event.Event;

public interface KafkaEvent<T> extends Event {
    String getTopic();
    T getBody();
}
