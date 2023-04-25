package com.liberty52.product.global.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liberty52.product.service.event.kafka.KafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public <T> void produceEvent(KafkaEvent<T> event) {
        kafkaTemplate.send(event.getTopic(), writeAsString(event.getBody()));
    }

    private <T> String writeAsString(T body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException from KafkaProducer.writeAsString(MyEvent)");
            throw new RuntimeException(e);
        }
    }
}
