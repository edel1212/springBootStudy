package org.zerock.board.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * @Descripciton  : DTO를 구성하는 기준은 기본적으로 화면에 전달하는 데이터 이거나 
 *                  반대로 화면 쪽에서 선달 되는 데이터를 기준으로 하기 때문에 
 *                  
 *                  ✔ 해당 DTO의 Target Entity Class 와 일치하지 않는 경우가 많다
 *                  
 *                  ✔ 현재 DTO Class 또한 Memeber에 대한 참조 구성은 하지 않고 작성되었음
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

    //Entity 와 다름 -- Member Class 에서 가져온 데이터를 넣을 변수
    private String writerEmail; 
    private String writerName;

    // Entity 기준 BaseEntity 에서 작성된것
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    
    //Entity 기준 연관관계가 없지만 JPQL의 JOIN ON 을 사용하여 가져올 변수 
    private int replyCount; //해당 게시글의 댓글 수
}
