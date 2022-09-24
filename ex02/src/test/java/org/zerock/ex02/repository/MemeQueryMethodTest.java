package org.zerock.ex02.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * @Description  : 🎈 주의사항으로 Delete 는 @Transactional 을 사용하지 않으면 에러가 밸상한다.
     *                    :::[ interface의 해당 메서드에 붙여 사용해도 괜찮음!! 선택사항임]
     *                   ❔ Error Msg = No EntityManager with actual transaction available for current thread - cannot reliably process 'remove' call;
     *                                  nested exception is javax.persistence.TransactionRequiredException
     *
     *                  - @Commit ?
     *                    최종결과를 커밋하기 위해 사용됩니다. 해당 어노테이션을 작성하지 않으면 테스트 코드의 deleteBy 는
     *                    기본적으로 롤백처리되어 결과에 반영되지가 않는다.
     *
     *                 ✔  해당 deleteBy는 실제 개발에서는 많이 사용되지 않는데 그 이유는 삭제쿼리를 한번에 날리는 것이 아닌
     *                    각 엔티티 객체를 하나씩 삭제하기 떄문에 비효일 적이기 때문이다.
     *                    => 따라서 해당 deleteBy 쿼리 메서드보다는 @Query 어노테이션 기능을 사용해서 이와같은
     *                        비효율적인 부분은 개선한다!
     *
     *
     * result  : Hibernate:
     *               select
     *                   memo0_.mno as mno1_0_,
     *                   memo0_.memo_text as memo_tex2_0_
     *               from
     *                   tbl_memo memo0_
     *               where
     *                   memo0_.mno < ?
     *           Hibernate:
     *               delete
     *               from
     *                   tbl_memo
     *               where
     *                   mno=?
     *           Hibernate:
     *               delete
     *               from
     *                   tbl_memo
     *               where
     *                   mno=?
     *           Hibernate:
     *               delete
     *               from
     *                   tbl_memo
     *               where
     *                   mno=?
     * */
    @Commit
    @Test
    void testDeleteQueryMethods() {
        memoRepository.deleteMemoByMnoLessThan(9L);
    }
}
