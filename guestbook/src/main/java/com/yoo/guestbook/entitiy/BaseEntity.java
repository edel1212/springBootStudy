package com.yoo.guestbook.entitiy;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @Description : Entity 관련 작업중에 중복되는 작업들 Ex ) 등록시간 , 수정시간과 같은
 *                자도으로 추가되고 변경되어야하는 컬럼들은 매번 프로그램안에서 처리하는 일은 번거롭기에
 *                자동으로 처리하도록 하자
 *
 *                ✔ @MappedSuperclass 가 지정된 클래스는 테이블로 생성되지 않는다.
 *                  - 실제 테이블은 해당 추상클래스를 상속한 Entity 의 class 가 생성된다.
 *                ✔ @EntityListeners 는 Entity 가 삽입, 삭제, 수정, 조회 등의 작업을 할 때 전
 *                   , 후에 어떠한 작업을 하기 위해 이벤트 처리를 위한 어노테이션이다
 *                  - JPA 내부에서 엔티티 객체가 생성/변경 되는것을 감지하는 역할은 AuditingEntityListener 로
 *                    이뤄진다.
 *                  - 🎈 단!  JPA를 이용하면서  AuditingEntityListener 를 활성화 하기 위해서는
 *                      프로젝트에 @EnableJpaAuditing 설정을 Application Start 하는 Method 에 추가해줘야한다!
 *
 * */
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
abstract class BaseEntity {

    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regeDate;

    @LastModifiedDate
    @Column(name ="moddate")
    private LocalDateTime modDate;

}
