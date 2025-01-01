package com.yoo.multipleDB.vo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class CatVO {
    private Long serial_number;
    private String name;
    private int age;
    private String breed;
}
