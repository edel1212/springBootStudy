package com.yoo.multipleDB.entity.sub;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class Cat {
    @Id
    private Long serialNumber;
    private String name;
    private int age;
    private String breed;
}
