package com.yener.kafkaconsumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yener.kafkaconsumer.service.CarEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CarEventConsumer {

    @Autowired
    private CarEventService carEventService;

    @KafkaListener(topics = {"car-events"})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        log.info("Consumer record : {}", consumerRecord);
        carEventService.processCarEvent(consumerRecord);
    }
}
