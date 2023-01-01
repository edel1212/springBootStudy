package com.yoo.toy.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@ToString
@Table(name = "tbl_dog")
@AllArgsConstructor
@Builder
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dnum;

    @Column(nullable = false, length = 20)
    private String name;

    @ColumnDefault("1")
    private int age;


}
