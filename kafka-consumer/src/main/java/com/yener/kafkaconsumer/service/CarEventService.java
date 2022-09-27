package com.yener.kafkaconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yener.kafkaconsumer.entity.CarEvent;
import com.yener.kafkaconsumer.repository.CarEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CarEventService {

    @Autowired
    private CarEventRepository carEventRepository;

    @Autowired
    ObjectMapper objectMapper;

    public void processCarEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        CarEvent carEvent = objectMapper.readValue(consumerRecord.value(), CarEvent.class);
        log.info("carEvent: {}" + carEvent);
        carEvent.getCar().setCarEvent(carEvent);
        carEventRepository.save(carEvent);
    }
}
