package org.zerock.ex02.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @Description : @Entity - ✔ Spring Data JPA의 interface를 사용하기 위해서는 해당 어노테이션이 필수이다.
 *                          ✔ 해당 클래스가 엔티티를 위한 클래스라는것을 표시함
 *                          ✔ 해당 클래스의 인스턴스들이 JPA로 관리되는 객체라는 것을 명시해줌
 *
 *              : @Table  - ✔ @Entity 어노테이션과 같이 사용할 수있는 어노테이션을 단어 그대로 테이블명 설정 뿐만아니라 인덱스 등을 생성하는 것도 가능하다.
 *
 *              : @Id 와 @GeneratedValue - ✔ @Id❔             - @Entity가 붙은 엔티티클래스는 PK에 해당하는 특정 필드를 지정하는것 [ 필수! ]
 *                                         ✔ @GeneratedValue❔ - @Id[PK]가 설정된 Key가 사용자가 입력하는 값이 아닌 자동으로
 *                                                               생성되는 번호를 사용하는 컬럼으로 설정함
 *
 *                                         ✔ @GeneratedValue(strategy = GenerationType.???) 종류 ❔
 *                                           - GenerationType.AUTO     : JPA 구현체가 생성 방식을 결정하게함 (Default)
 *                                           - GenerationType.IDENTITY : 사용하는 데이터베이스가 키 생성을 결정
 *                                           - GenerationType.SEQUENCE : 데이터베이스의 sequence 이용해서 결정 @SequenceGenerator 와 같이 사용
 *                                           - GenerationType.TABLE    : 키 생성 전용 테이블을 생성해서 키 생성. @TableGenerator 와 같이 사용
 *
 *              : @Column - ✔ 컬럼의 옵션이 필요한 경우 해당 어노테이션을 추가하여 설정이 가능하다.
 *                          ✔ 해당 어노테이션에는 다양한 속성이 추가 가능하다 [ 주로 사용되는것 ]
 *                            - nullable         : null 여부를 boolean으로 지정
 *                            - name             :객체 변수명과 Table의 컬럼명을 다르게 하고 싶은 경우 사용한다.
 *                            - length           : 컬럼의 길이
 *                            - columnDefinition : 기본값 지정
 *
 *              : Entity Class의 기본적 세팅 어노테이션
 *                  @Getter
 *                , @Builder
 *                , @AllArgsConstructor
 *                , @NoArgsConstructor - ✔ @Builder 를 이용해서  객체를 생성하기위해 AllArg, NoArg Constructor
 *                                         같이 사용해줘야 컴파일 시 에러가 없다.
 * 
 *             : 주의사항 - @Data , @Setter 를 사용하지 않는 이유는 Entity Class의 값은 직접적인 Class의
 *                         수정이 적을 수록 추후 개발에 용의 하기 때문이다
 *
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
