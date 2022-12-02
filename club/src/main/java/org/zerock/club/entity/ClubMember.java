package org.zerock.club.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity //Entity Class 지정
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ClubMember extends BaseEntity{

    @Id //PK
    private String email;

    private String password;

    private String name;

    private boolean fromSocial;


}
