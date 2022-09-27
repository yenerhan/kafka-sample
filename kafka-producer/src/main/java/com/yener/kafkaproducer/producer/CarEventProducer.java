package com.yener.kafkaproducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yener.kafkaproducer.model.CarEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class CarEventProducer {

    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;


    @Value("${topic.name.producer}")
    private String topicName;

    public void sendCarEvent(CarEvent carEvent) throws JsonProcessingException {
        Integer key = carEvent.getCarEventId();
        String value = objectMapper.writeValueAsString(carEvent);
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(topicName, key, value);
        future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }

            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }
        });
    }


    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Message Sent Successfully for the key : {} and the value is : {} , partion is {}", key, value, result.getProducerRecord().partition());
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.info("Error Sending Messages and the Exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.info("Error is onFailure: {} ", throwable.getMessage());
        }
    }
}
