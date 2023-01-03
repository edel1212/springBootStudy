package org.zerock.ex02.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
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

    // Query Method 사용 - 간단한 처리에만 사용한다 보면된다.
    
    // Mno 의 경우 단건이기에 타입을 맞춰준다.
    Memo findByMno(Long mno);
    
    // MemoText의 경우 다건이기에 List<T>로 지정
    List<Memo> findByMemoText(String memoText);

    // findBy "TargetColumn" "Between" "OrderBy" "TargetColumn" "Desc"
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    /**
     * @Description  : 위의 메서드의 경이 이름도 길고 혼동하기 쉽다
     *                 메서드 쿼리는 다행히도 Pageable 를 파라미터로 받아
     *                 사용이 가능하다
     *
     *                 ✔ Pageable 에서 정렬하여 사용!
     *
     *                 🎈 Pageable 을 사용 했으므로 Return 값은 Page가 됨!
     * */
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    /***
     * @Description  : 삭제 매서드 쿼리 __ ">" mno 가 10미만 데이터 삭제
     * */
    @Transactional
    void deleteMemoByMnoLessThan(Long num);

    /****************************************************************************/
    
    // @Query  어노테이션  사용 - 이 방법을 더 많이 사용함
    // - 메서드의 이름과 상관없이 메서드에 추가한 @Query 어노테이션을 통해 처리가 가능하다
    // - 필요한 데이터만 선별적으로 추찰하는 기능
    // - 데이터베이스에 맞는 순수한 SQL 을 사용하능
    // - insert, update, delete  처리할때는 (@Modifying 과 함께  사용 해야함!!)
    /**
     * 여기서 주의 깊게 보아야 하는 부분은 Table명은 tbl_memo지만 Entity Class 명으로 작성 되었다.
     * 컬럼명 또한 맴버변수 명으로 사용!
     */
    @Query("select m from Memo m order by m.mno desc")
    List<Memo> getListDesc();

    /**
     * @Description  : 주의해야 하는것은 해당 쿼리에서 사용되는 테이블명 및 컬럼명은
     *                 사용하려는 Entity Class의 Class명 변수 값 이다.
     * **/
    @Modifying
    @Transactional
    @Query("update Memo m set m.memoText = :memoText where m.mno = :mno")
    int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);
    
    /**
     * @Description  : 위의 방법을 사용하면 변수가 여러개일 경우 불편하다
     *                " # "  를 사용해서 객체 변수로 전달 받아 사용이 가능하다
     *
     *                 🎈 주의사항 - 1) #{param.mno} 가 아니라 #{#param.mno} 이다  #이 2개임!!
     *                              2) :#{#..} 앞에 " : " <<- rk 가 있음 !
     * */
    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno}")
    int updateMemoTestWithObj(@Param("param") Memo memo);

   /**
     * @Description : @Query 어노테이션에서도
     *                Pageable를 Parameter로 받아 페이징 처리와 정렬에 대한 부분을
     *                Pageable로 처리가 가능하다.
     *
     *                🎈 주의사항 - countQuery  = "Query문"을 작성하여 카운트를 처리를 해주면 페이징 시
     *                             카운트를 해주지만 안써줘도 자동으로 쿼리가 작성된다.
     *                             필수는 아니며 다음에 배울 Native Query를 사용시는 필수이다.
     *
     *                             단순히 데이터 뿐 아니라 페이지 처리에 필요한 모든 내용을 처리합니다.
     *
     *                           설명 : 화면에 페이징 처리를 위해서는 전체 데이터의 수를 파악할 필요가 있습니다.
     *                                  이를 이용해서 전체 몇 개의 페이지로 만들어지는지
     *                                  이전/다음 페이지의 정보 들도 같이 처리합니다.
     *                                  <strong>예를 들어 데이터가 부족하면 카운트 쿼리를 실행하지 않습니다.</strong>
     * */
    @Query(value = "select m from Memo m where m.mno > :mno"
            , countQuery = "select count(m) from Memo m where m.mno > :mno"
    )
    Page<Memo> getListWithQuery(Long mno, Pageable pageable);

    /**
     * @Description  : @Query 어노테이션의 장점은 return 타입을 Object[]로 사용이 가능하다는 점이다!
     *
     *                하위 value = "query"를 확인해보면 내가 만든 Memo entity class 에는 날짜 타입의
     *                컬럼을 지정해주지 않았지만  CURRENT_CATE, CURRENT_TIME, CURRENT_TIMESTAMP
     *                를 사용하여 현재 데이터베이스의 시간을 구할 수 있다.
     *
     *                이처럼 엔티티에 타입이 존재하지 않을 경우 사용이 가능하다
     *
     *                🎈 쿼리 메서드에서는 Object 타입을 반환 불가능함!! entity class 로 지정한 것만 가능
     *
     * */
    @Query(value = "select m.mno, m.memoText, CURRENT_DATE from Memo m where m.mno > :mno"
            , countQuery = "select count(m) from Memo m where m.mno > : mno"
    )
    Page<Object[]> getListWithQueryObject(@Param("mno") Long mno, Pageable pageable);
    
    /**
     * @Description : 데이터베이스이 고유의 SQL문을 그대로 활용하는 방법
     * 
     *                 JPA 자체의 데이터베이스에 독립적으로 구현 가능하다는 장점을 잃어버리긴 하지만
     *                 
     *                 경우에 따라서 복잡한 JOIN 구문 등을 처리하기 위해 어쩔수 없는 경우에 선택 사용함
     *
     *                 🎈 주의사항 : nativeQuery 의 경우 table 명은 Database 의 Table 명을 따라간다
     * */
    @Query(value = "select * from tbl_memo where mno > 0"
            , nativeQuery = true //네이티브 Query를 사용하겠다!
    )
    List<Object[]> getNativeResult();

}
