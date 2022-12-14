package com.yoo.toy.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Getter
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude // 연관관계는  ToString 에서 항상 제외해 줘야함!
    private Member writer; //연관관계 지정

}
