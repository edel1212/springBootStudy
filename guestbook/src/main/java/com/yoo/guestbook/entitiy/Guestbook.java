package com.yoo.guestbook.entitiy;

import lombok.*;

import javax.persistence.*;

/**
 * @Description : 해당 Entity Class 는 BaseEntity ( 작성날짜, 수정날짜 )를 만다는
 *                추상 Class 를 상송받음!
 * */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Guestbook extends  BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //기본키 자동생성
    private Long gno;

    @Column(length = 100,nullable = false)
    private String title;

    @Column(length= 1500, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

}
