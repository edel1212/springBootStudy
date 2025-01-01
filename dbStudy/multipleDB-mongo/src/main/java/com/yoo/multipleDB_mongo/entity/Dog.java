package com.yoo.multipleDB_mongo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.Comment;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Entity
public class Dog {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK")
    @Id
    private Long serialNumber;

    private String name;

    private int age;
}
