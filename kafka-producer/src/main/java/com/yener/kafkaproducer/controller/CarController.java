package com.yener.kafkaproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yener.kafkaproducer.model.CarEvent;
import com.yener.kafkaproducer.producer.CarEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/car")
public class CarController {

    @Autowired
    CarEventProducer carEventProducer;


    @PostMapping("/saveCar")
    public ResponseEntity<CarEvent> saveCar(@RequestBody CarEvent carEvent) throws JsonProcessingException {
        carEventProducer.sendCarEvent(carEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(carEvent);
    }

    @PutMapping("/updateCar")
    public ResponseEntity<?> updateCar(@RequestBody CarEvent carEvent) throws JsonProcessingException {
        if(carEvent.getCarEventId()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass library event Id");
        }
        carEventProducer.sendCarEvent(carEvent);
        return ResponseEntity.status(HttpStatus.OK).body(carEvent);
    }
}
