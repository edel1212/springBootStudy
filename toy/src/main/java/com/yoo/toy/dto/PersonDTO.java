package com.yoo.toy.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true) // 만들어진 객체를 toBuilder 수정이 가능하게 끔 설정
public class PersonDTO {

    private String name;

    private int age;

    private boolean gender;

}
