package com.yoo.webflux_study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResDTO {
    private String code;
    private String message;
    // Response 필드 중 해당 필드가 DTO에 없을 경우 에는 없는 상태로 반환 - 아래 필드가 주석일 경우
    private String variable;
    // Response Data 중 DTO에만 해당 필드가 있을 경우 null 반환
    // private String notMatchField;
}
