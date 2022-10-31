package org.zerock.mreview.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@Table(name = "m_member") // 이전 프로젝트의 member 와 겹치므로 TableName 지정
public class Member extends  BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mid;

    private String email;

    private String pw;

    private String nickname;
}
