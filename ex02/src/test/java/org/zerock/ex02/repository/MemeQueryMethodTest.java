package org.zerock.ex02.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.ex02.entity.Memo;

import java.util.List;

/**
 * @Description  : 해당 Test 에서 해볼것은 쿼리 메서드 기능과 
 *                 JPQL(Java Persistence Query Language)라는 객체 지향쿼리 에대한 기능이다
 *                
 *                MemeRepository.java 에서 테스트했던 페이징 기능만으로는
 *                검색 조건을 넣을수가 없다는 단점이 있었다 ex __> name like %정호
 *                따라서 해당 기능을 해줄 수있는 기능을 Spring Data JPA 에서 제공한다
 *
 *                1) 쿼리 메서드 : 메서드의 이름 자체가 쿼리의 구문으로 처리된는 기능
 *                   ✔ 메서드 자체가 Query문이 되는 기능이다
 *                   ✔ 쿼리 메서드는 주로 "findBy" , "getBy.."로 시작하고 이후에 필요한 필드 조건
 *                     이나 And, Or 와같은 키워드를 이용해서 메서드의 이름 자체로 Query 문을 만든다.
 *                   @see MemoRepository : JpaRepository를 상속받은 Interface에 메서드를 구현하여 진행
 *
 *                2) @Query    : SQL 과 유사하게 엔티티클래스이 정보를 이용해서 쿼리 작성이 가능
 *                3) Querydsl 등의 동적쿼리 처리기능
 * */
@SpringBootTest
public class MemeQueryMethodTest {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testQueryMethod(){
        /**
         * 🎈 주의사항 : 쿼리 메서드의 주의할점은 메서드명에서 오타가 있으면 쿼리가
         *             안만들어는 에러가난다!
         *
         * @result : select
         *              memo0_.mno as mno1_0_,
         *              memo0_.memo_text as memo_tex2_0_
         *           from
         *              tbl_memo memo0_
         *           where
         *              memo0_.mno between ? and ?
         *           order by
         *              memo0_.mno desc
         *
         *          -------------------------------
         *
         *          Memo(mno=80, memoText=Sample...80)
         *          Memo(mno=79, memoText=Sample...79)
         *          Memo(mno=78, memoText=Sample...78)
         *          Memo(mno=77, memoText=Sample...77)
         *          Memo(mno=76, memoText=Sample...76)
         *          Memo(mno=75, memoText=Sample...75)
         *          Memo(mno=74, memoText=Sample...74)
         *          Memo(mno=73, memoText=Sample...73)
         *          Memo(mno=72, memoText=Sample...72)
         *          Memo(mno=71, memoText=Sample...71)
         *          Memo(mno=70, memoText=Sample...70)
         *
         * */
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);
        list.forEach(System.out::println);
    }

    /***
     *
     * result : Hibernate:
     *              select
     *                  memo0_.mno as mno1_0_,
     *                  memo0_.memo_text as memo_tex2_0_
     *              from
     *                  tbl_memo memo0_
     *              where
     *                  memo0_.mno between ? and ?
     *              order by
     *                  memo0_.mno desc limit ?
     *          Hibernate:
     *              select
     *                  count(memo0_.mno) as col_0_0_
     *              from
     *                  tbl_memo memo0_
     *              where
     *                  memo0_.mno between ? and ?
     *
     *    -------------------------------------------------
     * 
     *          Memo(mno=50, memoText=Sample...50)
     *          Memo(mno=49, memoText=Sample...49)
     *          Memo(mno=48, memoText=Sample...48)
     *          Memo(mno=47, memoText=Sample...47)
     *          Memo(mno=46, memoText=Sample...46)
     *          Memo(mno=45, memoText=Sample...45)
     *          Memo(mno=44, memoText=Sample...44)
     *          Memo(mno=43, memoText=Sample...43)
     *          Memo(mno=42, memoText=Sample...42)
     *          Memo(mno=41, memoText=Sample...41)
     *
     * */
    @Test
    public void testQueryMethodWithPageable(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(40L,50L,pageable);

        result.forEach(System.out::println);

    }

}
