package com.yoo.toy.dto;

import com.yoo.toy.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BoardDTO {
    private Long bno;

    private String title;

    private String content;

    //Entity 와 다름 -- Member Class 에서 가져온 데이터를 넣을 변수
    private String writerEmail;
    private String writerName;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private int replyCount; //해당 게시글의 댓글 수
}
