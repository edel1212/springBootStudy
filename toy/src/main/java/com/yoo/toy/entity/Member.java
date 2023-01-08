package com.yoo.toy.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class Member extends BaseEntity{

    @Id
    private String email;

    private String password;

    private String name;

}
