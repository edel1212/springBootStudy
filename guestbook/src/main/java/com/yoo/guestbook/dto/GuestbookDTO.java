package com.yoo.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data //자유롭게 데이터를 변경할 수 있게 하기 위함.
public class GuestbookDTO {

    private Long gno;
    private String title;
    private String content;
    private String writer;
    //BaseEntity에 추가된 변수
    private LocalDateTime regDate,modDate;

}
