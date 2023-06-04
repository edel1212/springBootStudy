package com.yoo.loginServer.entity.member;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    private String id;
    private String pw;
    private String name;
    private String role;
}
