package org.zerock.ex02.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex02.entity.Memo;

import java.util.List;

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
    // findBy "TargetColumn" "Between" "OrderBy" "TargetColumn" "Desc"
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    /**
     * @Description  : 위의 메서드의 경이 이름도길도 혼동하기 쉽다
     *                 메서드 쿼리는 다행히도 Pageable 를 파라미터로 받아
     *                 사용이 가능하다
     *
     *                 ✔ Pageable 에서 정렬하여 사용!
     *
     *                 🎈 Pageable 을 사용 했으므로 Return 값은 Page가 됨!
     * */
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    /***
     * @Description  : 삭제 매서드 쿼리 __> mno 가 10보다 작은 데이터 삭제
     * */
    void deleteMemoByMnoLessThan(Long num);

}
