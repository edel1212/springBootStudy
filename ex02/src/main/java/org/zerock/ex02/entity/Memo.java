package org.zerock.ex02.entity;

import lombok.ToString;

import javax.persistence.*;

/**
 * @Description : @Entity - ✔ Spring Data JPA에서는 반드시 해당 어노테이션을 추가해야만한다
 *                          ✔ 해당 클래스가 엔티티를 위한 클래스라는것을 표시함
 *                          ✔ 해당 클래스의 인스턴스들이 JAP로 관리되는 객체라는 것을 명시해줌
 *                          
 *              : @Table  - ✔ @Entity 어노테이션과 같이사용할 수있는 어노테이션을 단어 그대로 테이블명 설정 뿐만아니라 인뎃스 등을 생성하는것고 가능함ㅎ
 * */
@Entity
@Table(name = "tbl_memo")
@ToString
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;
}
