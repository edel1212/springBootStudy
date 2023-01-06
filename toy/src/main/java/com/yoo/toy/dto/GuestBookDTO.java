package com.yoo.toy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data //자유롭게 데이터를 변경 할 수있게 하기 위함.
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuestBookDTO {

    private Long gnum;
    private String title;
    private String content;
    private String writer;
    //BaseEntity에서 작성된 변수
    private LocalDateTime regDate;
    private LocalDateTime modDate;

}
