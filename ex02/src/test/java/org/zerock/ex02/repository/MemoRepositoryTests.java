package org.zerock.ex02.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.zerock.ex02.entity.Memo;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @Description  : ✔중요 : 2 ~ 3시간 삽질함 이유
 *                 Test Class 파일명과 현재 java Test 의 Class 명이 겹쳤음 ...
 *                 그래서 생성자 만들때 에러가 났음 ..
 * */
@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void test(){
        System.out.println(memoRepository.getClass().getName());
    }


    /**********************************************/
    /**************INSERT TEST*************/
    /**********************************************/

    /**
     * @Description  : IntStream 을 이용하여 100번의 반복동안
     *                 Memo Entity Class 의 builder 메서드를 이용하여 객체
     *                 변수를 만들어준다
     *
     *                 ☑ 여기서 주의할점은 momeText 의 경우는 NotNull 이기 때문에 값이 꼭 필요함
     *
     *                 이 후 MemoRepository [JpaRepository 상속받음] 해당 Interface의
     *                 save 기능을 이용하여 Insert 시킴!
     * */
    @Test
    public void testInsertDummies(){
        IntStream.range(1,100).forEach(i->{
            Memo momo = Memo.builder().memoText("Sample..." + i).build();
            memoRepository.save(momo);
        });
    }


    /**********************************************/
    /**************SELECT TEST*************/
    /**********************************************/

    /*************************************************************************************************/
    /**
     * @Description  : 두가지 테스트 전부 원하는 번호의 정보를 가져오는 것은 같지만
     *                 각각의 사용 메서드에 차이가있다
     *
     *                 두 매서드의 차이는 동작 하는 방식에 차이가 있는데
     *                 데이터베이스를 먼저 이용하는지 아니면 필요한 순간까지 미뤄서 이용하는지에 대한 차이가 있다
     *                 ✔ 여기서 주의 해야할것은 필요 순간이란 실제로 값을 사용하는 순간을 말하는것이다
     *                    객체뱐수 생성 때가 아니라 예제 코드에서의 System.out.println()임!!
     *
     *                 1) findById(targetId) : 해당 메서드의 쿼리가 먼저 찍힌후  ====== println 이
     *                                         찍힌것이 확인 가능하다!
     *
     *                 2) getOne(targetId)  : 해당 메서드의 실행경우 ======= 로그 가 찍힌 후
     *      *                                 실제 println 시 쿼리가 나오는걸 확인이 가능하다
     * */

    @Test
    public void testSelect(){
        Long mno = 50L;
        Optional<Memo> result =  memoRepository.findById(mno); // 50번의 데이터
        System.out.println("===========================================");
        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    /**
     * @Description  :  getOne()를 사용하기 위해서는 Transaction 이 필이함
     *
     *        Error Log : no Session - org.hibernate.LazyInitializationException: could not initialize proxy [org.zerock.ex02.entity.Memo#100] - no Session
     */
    @Transactional
    @Test
    public void testSelect2(){
        Long mno = 100L;
        Memo  memo = memoRepository.getOne(mno);
        System.out.println("============================================");
        System.out.println(memo);
    }

    /*************************************************************************************************/

    /**********************************************/
    /**************UPDATE TEST*************/
    /**********************************************/

    /**
     * @Description : update 도 insert 와 마찬가지로 save 메서드를 사용한다
     *                해당 테스트 실행 log를 보면
     *
     *                먼저 Select가 된 후
     *                Hibernate:
     *                  select
     *                      memo0_.memo_text as memo_tex2_0_0_
     *                      memo0_.mno as mno1_0_0_,
     *                  from
     *                      tbl_memo memo0_
     *                  where
     *                      memo0_.mno=?
     *
     *                해당 데이터가 있으면 update 없으면 Insert 한다!
     *                Hibernate:
     *                  insert
     *                      into
     *                  tbl_memo
     *                      (memo_text)
     *                  values
     *                      (?)
     *
     *              ✔ 이러한 구동의 원리는 JPA는 엔티티의 객체들을 메모리상에 보관하려하기 때문에
     *                특정한 엔티티 객체가 존재하는지 확인하는 Select 가 먼저 실행 되고 해당 @Id를 가지는
     *                엔티티 객체가 있다면 update 혹은 Insert가 실행되는 것이다.
     *
     *
     * **/
    @Test
    public void testUpdate(){
        final Long MNO = 100L;
        Memo memo = Memo.builder().mno(MNO).memoText("update Text ! Test !!!!").build();
        System.out.println(memoRepository.save(memo));
    }


    /**********************************************/
    /**************DELETE TEST*************/
    /*********************************************/
    /**
     * @Description : 해당 테스트는 Delete 테스트로
     *                메서드가 실행시 save() 메서드와같이
     *                먼저 Select 로 해당 ID의 데이트를 확인한 후 삭제한다
     *                
     *                만약 ID 가없는 값을 삭제를 요청할 경우
     *
     *                org.springframework.dao.EmptyResultDataAccessException 에러가 발생한다.
     * */
    @Test
    void testDelete() {
        Long mno = 100L;
        memoRepository.deleteById(mno);
    }


    /**********************************************/
    /**************Paging TEST*************/
    /*********************************************/
    @Test
    void testPageDefault() {
        //1페이지에 10개씩
        Pageable pageable = PageRequest.of(0,10);
        //findAll 가 필요로 하는 매개변수는 Pageable 이다!
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);
    }
}   
