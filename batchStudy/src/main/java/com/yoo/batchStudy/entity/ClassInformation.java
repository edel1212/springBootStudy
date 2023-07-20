package com.yoo.batchStudy.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class ClassInformation {

    @Id
    private String name;

    private Long studentCnt;


}
