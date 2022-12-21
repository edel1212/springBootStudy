package org.zerock.bimovie.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass //Table 생성용이 아닌 상속용 Class
@EntityListeners(value = AuditingEntityListener.class) //JPA 생성,수정 시 감지
@Getter
public class BaseEntity {
    
    @CreatedDate //생성시점
    @Column(name ="regdate", updatable = false)
    private LocalDateTime regeDate;

    @LastModifiedDate //수정시점
    @Column(name ="moddate")
    private LocalDateTime modDate;

}
