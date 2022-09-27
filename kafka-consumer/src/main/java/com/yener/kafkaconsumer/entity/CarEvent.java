package com.yener.kafkaconsumer.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class CarEvent {

    @Id
    @GeneratedValue
    private Integer carEventId;

    @OneToOne(mappedBy = "carEvent", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private Car car;
}
