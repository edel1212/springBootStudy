package org.zerock.ex02.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @Description : @Entity - ✔ Spring Data JPA에서는 반드시 해당 어노테이션을 추가해야만한다
 *                          ✔ 해당 클래스가 엔티티를 위한 클래스라는것을 표시함
 *                          ✔ 해당 클래스의 인스턴스들이 JAP로 관리되는 객체라는 것을 명시해줌
 *                          
 *              : @Table  - ✔ @Entity 어노테이션과 같이사용할 수있는 어노테이션을 단어 그대로 테이블명 설정 뿐만아니라 인뎃스 등을 생성하는것고 가능함
 *
 *              : @Id 와 @GeneratedValue - ✔ @Id❔ - @Entity가 붙은 엔티티클래스는 PK에 해당하는 특정 필드를 지정하는것 [ 필수! ]
 *                                         ✔ @GeneratedValue❔ - @Id가 설정된 PK가 사용자가 입력하는 값이 아닌 자동으로
 *                                                               생성되는 번호를 사용하는 컬럼이라 지정하는것
 *                                           
 *                                         ✔ @GeneratedValue(strategy = GenerationType.???) 종류 ❔
 *                                           - GenerationType.AUTO     : JPA 구현체가 생성 방식을 결정하게함 (Default)
 *                                           - GenerationType.IDENTITY : 사용하는 데이터베이스가 키 생성을 결정
 *                                           - GenerationType.SEQUENCE : 데이터베이스의 sequence 이용해서 결정 @SequenceGenerator 와 같이 사용
 *                                           - GenerationType.TABLE    : 키 생성 전용 테이블을 생성해서 키 생성. @TableGenerator 와 같이 사용
 *
 *              : @Column - ✔ 추가적으로 컬럼이 필요한 경우 해당 어노테이션을 ㅗ추가 가능하다
 *                          ✔ 해당 어노테이션에는 다양한 속성이 추가 가능하다 [ 주로 사용되는것 ]
 *                            - nullable         : null 여부 boolean 지정
 *                            - name             :객체명과 DB 컬럼명을 다르게 하고 싶은 경우, DB 컬럼명으로 설정할 이름을 name 속성으로 적는다.
 *                            - length           : 길이
 *                            - columnDefinition : 기본값 지정
 * */                                      
@Entity
@Table(name = "tbl_memo")
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 200, nullable = false)
    private String memoText;
}
