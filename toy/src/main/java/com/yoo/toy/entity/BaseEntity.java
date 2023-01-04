package com.yoo.toy.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @Description : @EnableJpaAuditing 가 없을 경우 아래 2개의 값에는 null 값이 들어간다!
 *                  - @CreatedDate
 *                  - @LastModifiedDate
 *
 *
 *                 | @EntityListeners 란?
 *                    JPA Entity에서 이벤트가 발생할 때마다 특정 로직을 실행시킬 수 있는 어노테이션입니다.
 *                    \@EntityListeners(AuditingEntityListener.class) 즉, AuditingEntityListener 클래스가
 *                     callback listener로 지정되어 Entity에서 이벤트가 발생할 때마다 특정 로직을 수행하게 됩니다.
 *
 *                | @EnableJpaAuditing란?
 *                  JPA Auditing(감시, 감사) 기능을 활성화하기 위한 어노테이션입니다.
 */
@MappedSuperclass // 테이블 생성 Class가 아님
@EntityListeners(AuditingEntityListener.class) //엔티티가 이벤트가 발생할때마다 감지
abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    private LocalDateTime modDate;

}
