package com.yoo.exGraphQL.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberDTO {
    private String email;
    private String password;
    private String name;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
