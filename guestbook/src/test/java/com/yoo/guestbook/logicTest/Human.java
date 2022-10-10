package com.yoo.guestbook.logicTest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Human {

    private String name;

    private int age;

    public  Human(){
        this.name = "unknown";
        this.age = 10;
    }

}
