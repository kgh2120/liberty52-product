package com.liberty52.product.service.event;

import com.liberty52.product.global.adapter.S3Uploader;
import com.liberty52.product.global.adapter.kafka.KafkaProducer;
import com.liberty52.product.service.event.internal.ImageRemovedEvent;
import com.liberty52.product.service.event.kafka.KafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
/**
 *
 */
public class ApplicationEventHandler {
    private final KafkaProducer kafkaProducer;
    private final S3Uploader s3Uploader;

    @EventListener
    public void handleToKafkaEvent(KafkaEvent<?> event) {
        kafkaProducer.produceEvent(event);
    }

    @EventListener
    public void deleteImageInS3OnCustomProductRemoved(ImageRemovedEvent event) {
        String imageUrl = event.getBody().url();
        try {
            s3Uploader.delete(imageUrl);
            log.info("An Image is deleted from s3. The url was {}", imageUrl);
        } catch (Exception e) {
            log.error("Unexpected Error in {}. Trying to delete image in s3. The url was {}", this.getClass().getName(), imageUrl);
            e.printStackTrace();
        }
    }
}
