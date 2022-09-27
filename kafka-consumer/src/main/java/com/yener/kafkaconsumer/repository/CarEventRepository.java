package com.yener.kafkaconsumer.repository;

import com.yener.kafkaconsumer.entity.CarEvent;
import org.springframework.data.repository.CrudRepository;

public interface CarEventRepository extends CrudRepository<CarEvent, Integer> {
}
