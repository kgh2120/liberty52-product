package com.liberty52.product.service.event.kafka;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public abstract class KafkaEventBase<T> extends ApplicationEvent implements KafkaEvent<T> {
    protected String topic;
    protected T body;

    public KafkaEventBase(Object source, String topic, T body) {
        super(source);
        this.topic = topic;
        this.body = body;
    }
}
