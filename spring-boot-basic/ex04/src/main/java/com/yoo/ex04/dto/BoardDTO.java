package com.yoo.ex04.dto;

import lombok.*;

import java.time.LocalDateTime;


/**
 * @Descripciton  : DTO를 구성하는 기준은 기본적으로 화면에 전달하는 데이터 이거나 
 *                  반대로 화면 쪽에서 전달 되는 데이터를 기준으로 하기 때문에 
 *                  
 *                  ✔ 기존 Entity Class 와 일치하지 않는 경우가 많다
 * */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    
    private Long bno;

    private String title;

    private String content;

    //기존 Board Entity 다른 추가사항 -- Member 객체에서 가져온 데이터를 넣을 변수
    private String writerEmail; 
    private String writerName;

    private LocalDateTime regDate;
    private LocalDateTime modDate;
    
    //기존 Board Entity 
    //다른 추가사항 -- 연관관계가 없지만 JOIN을 이용해 가져온 데이터를 넣을 변수
    private int replyCount; //해당 게시글의 댓글 수
}
