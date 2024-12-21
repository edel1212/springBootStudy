package com.yoo.ex04.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {

    private Long rno;

    private String text;

    private String replyer; //게시글 번호

    private Long bno;

    // Entity 기준 BaseEntity 에서 작성된것
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}

