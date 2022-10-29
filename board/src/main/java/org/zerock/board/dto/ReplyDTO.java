package org.zerock.board.dto;

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

    private String replyer;

    private Long bno;

    // Entity 기준 BaseEntity 에서 작성된것
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}

