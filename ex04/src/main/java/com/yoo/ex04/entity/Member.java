package com.yoo.ex04.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Member extends BaseEntity{
    @Id
    private String email;

    private String password;

    private String name;

}
