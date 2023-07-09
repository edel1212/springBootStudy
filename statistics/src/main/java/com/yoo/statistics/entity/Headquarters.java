package com.yoo.statistics.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Headquarters {

    // 건물 관리 코드
    @Id
    private String buildCode;

    // 건물명
    private String buildName;

    // 파일 위치
    private String filePath;

    // 확장자
    private String fileExtension;

}
