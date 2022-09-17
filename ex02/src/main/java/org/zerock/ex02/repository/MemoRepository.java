package org.zerock.ex02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex02.entity.Memo;

/**
*  @Description : 1) JpaRepository는 인테페이스이며 Spring Data
 *                   JPA는 이를 상속하는 인터페이스를 상속하는 인터페이스를 선언하는 것만으로
 *                   모든 처리가 끝난다.
 *
 *                2) JpaRepository를 사용할 때는 엔티티의 타입정보(Meno) 와 @Id의 타입을 제네릭에
 *                   지정해 줘야한다 < Memo , Long >  :: < 엔티티Class , PK Type >
 *
 *                3) Spring Data JPA는 인터페이스 선언 만으로도 자동으로 빈에 등록된다.
 *
* */
public interface MemoRepository extends JpaRepository<Memo, Long> {
}
