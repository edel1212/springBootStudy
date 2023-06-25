package com.yoo.statistics.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Electricity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eno;

    private Double value;

    private LocalDateTime regDate;


}
