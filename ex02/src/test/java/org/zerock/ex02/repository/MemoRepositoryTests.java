package org.zerock.ex02.repository;


import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    /**
     * @Description : Spring Data JPA 에서 페이징 처리와 정렬은 findAll()이란 메서드를 사용한다
     *
     *                findAll()는 jpaRepository 인터페이스의 상위 PagingAndSort Repository 의 메서드이며
     *                 - 메서드에 전달되는 파라미터는 Pageable 이라는 타입의 객체에 의해 실행되며 해당 메서드로
     *                   쿼리가 작성된다
     *                
     *                또한 finaAll()의 return Type 은 Page<T> :: 페이징 시
     *                    , Iterable<T>  :: 정렬 return 시 
     *                    이며  리턴 타입을 Page<T>로 지정하는 경우 반드시 파라미터는 Pageable 이어여한다!
     *
     *              -----------------------------------------------------------------------------------
     *
     *              : 페이징 처리에  가장 중요한 존재는 Pageable Interface 이며 해당 Interface 는
     *                페이지 처리에 필요한 정보를 전달하는 용도의 타입의 인터페이스 이기 때문에
     *
     *                실제 객체를 생성할 때는 PageRequest 라는 Class 를 사용한다
     *                -  해당 Class 는 protected 로 선언 되어 있기 떄문에 new 를 사용하여 객체변수 생성이 불가능하다
     *                - 객체를 생성하기 위해서는 static Method 인 of()를 이용해야한다
     *                - of()의 Overloading Method
     *                   - of(int page, int size) : 0부터 시작하는 페이지 번화와 개수 [ 정렬 X ]
     *                   - of(int page, int size, Sort direction, String ... props ) : 0부터 시작
     *                                                                                하는 페이지 번화와 개수 
     *                                                                                , 정랼의 방향과 정렬의 기준
     *                  - of(int page, int size, Sort sort) : 페이지 번호와 개수, 정렬에 관한 정보
     *
     *               ----------------------------------------------------------------------------------
     *
     *               ✔ 여기서 주의 깊게 볼 부분은 Return Type 인 Page 이다 그 이유는
     *                 - 해당 타입은 단순히 해당 목록만을 가져오는데 그치지 않고 실제 페이지 처리에 필요한 전체 데이터의
     *                   개수를 가져오는 쿼리 역시 같이 처리하기 때문이다
     *                   ( 데이터가 충분하지 않다면 데이터의 개수를 가져오는 쿼리를 실행하지 않는다. )
     *
     * */
    @Test
    void testPageDefault() {
        //1페이지에 10개씩 ✔ 0페이지가 1페이지 이다!
        Pageable pageable = PageRequest.of(0,10);
        //findAll 가 필요로 하는 매개변수는 Pageable 이다!
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);
    }

    @Test
    void testPageDefaultDtls() {
        Pageable pageable = PageRequest.of(0,10);
        
        Page<Memo> result = memoRepository.findAll(pageable);
        
        System.out.println(result);
        
        System.out.println("----------------------------------------------");

        System.out.println("total Pages : " + result.getTotalPages()); // 총 페이지
        System.out.println("Total Count : " + result.getTotalElements()); //전체 개수
        System.out.println("Page Number : " + result.getNumber());  //현재 페이지 번호
        System.out.println("Page Size   : " + result.getSize() ); //페이지당 데이터 개수
        System.out.println("Has Nest Page ? : " + result.hasNext()); //다음페이지 유무
        System.out.println("First Page? : " + result.isFirst()); // 시작페이지 여부

        System.out.println("----------------------------------------------");
    
        /**
         * 실제 페이지의 데이터를 처리하는 것은 getContent() 메서드이다
         * */
        result.getContent().forEach(System.out::println);
    }


    @Test
    void testSort() {
        Sort sort1 = Sort.by("mno").descending(); //DESC - 내림차순
        Pageable pageable = PageRequest.of(0,10,sort1); 
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println("DESC 내림차순 descending() 사용");
        result.getContent().forEach(System.out::println);

        System.out.println("--------------------------------------");
        
        System.out.println("ASC 오름차순 ascending() 사용");
        Sort sort2 = Sort.by("memoText").ascending(); //ASC 오름차순
        pageable = PageRequest.of(0,10,sort2);
        result = memoRepository.findAll(pageable);
        result.getContent().forEach(System.out::println);

        System.out.println("--------------------------------------");
        System.out.println("정렬 방법 2개 혼합 mno Desc And memoText ASC");
        Sort sortAll = sort1.and(sort2); // and를 이용해서 두 정렬을 연결함
        pageable = PageRequest.of(0,10, sortAll);
        result = memoRepository.findAll(pageable);
        result.getContent().forEach(System.out::println);
        
    }




}
