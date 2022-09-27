package com.yener.kafkaconsumer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Car {

    @Id
    private Integer id;
    private String name;
    @OneToOne
    @JoinColumn(name = "carEventId")
    private CarEvent carEvent;


}
