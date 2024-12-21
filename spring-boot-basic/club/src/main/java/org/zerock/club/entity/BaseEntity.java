package org.zerock.club.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass //Table 이 생성되지 않는 Entity Class
@EntityListeners(value = AuditingEntityListener.class) // CRUD 시 전, 후 작업하기위한 이벤트 처리 - AuditingEntityListener.class :: JAP 생성 변경 감지
@Getter
public class BaseEntity {
    
    @CreatedDate //생성 시점
    @Column(name ="regdate", updatable = false) //컬럼명 및 설정 추가
    private LocalDateTime regeDate;
    
    @LastModifiedDate //수정 시점
    @Column(name = "moddate")
    private LocalDateTime modDate;
    
}
