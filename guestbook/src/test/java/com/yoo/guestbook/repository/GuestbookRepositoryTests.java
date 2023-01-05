package com.yoo.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.yoo.guestbook.entitiy.Guestbook;
import com.yoo.guestbook.entitiy.QGuestbook;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1,300).forEach(i->{
            Guestbook guestbook = Guestbook.builder()
                    .title("title..." + i)
                    .content("Content..." + i)
                    .writer("Writer...." + i)
                    .build();
            log.info(guestbookRepository.save(guestbook));
        });
    }

    /**
     * 해당 테스트틀 진행하면 기존 Entity Class 에서 상속받은 추상 Class 인  BaseEntity 의
     * moddate 가 Update 된것을 확인할 수 있다!
     * */
    @Test
    public void updateTest(){
        Optional<Guestbook> result = guestbookRepository.findById(300L);
        if(result.isPresent()){// 존재할경우
          Guestbook guestbook = result.get();

          guestbook.setContent("Changed content!!");
          guestbook.setTitle("Changed title!!");
          guestbookRepository.save(guestbook);
        }
    }

    @Test
    public void updateTestWhyUseSetter(){
        Guestbook guestbook = Guestbook.builder().gno(200L).content("Changed!!").title("Title Change!!").writer("Yoo!!").build();
        guestbookRepository.save(guestbook);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Querydsl Test Start!
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @Description :  ✔ 1 ) 동적으로 처리하기 위해 Q도메인 Class를 얻어온다, Q도메인 Class를 이용하면
     *                      엔티티 클래스에 선언된 title, content 같은 필드 변수 활용이 가능해짐!
     *                      ---  쉽게 설명하면 객체 변수를 만들어 각각의 엔티티 변수에 접근하여
     *                           contains() 같은 조건에 맞는 메서드 사용이 가능
     *
     *                ✔ 2 ) BooleanBuilder는 Where문에 들어가는 조건을 넣어주는 컨테이너로 생각하면 된다.
     *                      --- 쉽게 설명하면 Where 문의 조건을 넣을 객체
     *
     *                ✔ 3) 원하는 조건을 만들어준다
     *                     해당 만들어준 조건의 객체 Type은 BooleanExpression 이어야한다
     *                      --- 2번에서 만든 builder에 넣어줘야는 객체
     *
     *                ✔ 4) 만들어진 조건은 where 문에 and 또는 or 등으로 합쳐준다.
     *                     --- 체이닝 가능함! 뒤에 계속 이어붙여서 조건을 만들어줄 수 있음
     *                      ✅ 중요 포인트는 해당 객체의 파라미터로는 Q도메인의 Predicate가 들어가는것이다
     *                          Java의 predicate 람다식이 아님!
     *
     *                ✔ 5) BooleanBuilder 는 GuestBookRepository 에 추가된
     *                     QuerydslPredicateExcutor 인터페이스에 의해 findAll()을 사용할 수있다.
     *
     *  - 결과값 : title에 "1"이 들어가있는 0페이지 ~ 10개 gnum으로 정렬 된 데이터
     * */
    @Test
    public void testQuery1(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook; //1

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder(); //2

        BooleanExpression expression = qGuestbook.title.contains(keyword); //3

        builder.and(expression); //4

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); //5

        result.stream().forEach(System.out::println);

    }

    /**
     * @Description  : 해당 테스트 목적 -> 다중 항목 검색 테스트
     *
     * */
    @Test
    public void testQuery2(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("gno").descending());
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);
    
        //두가지 조건을 or 조건으로 합침
        BooleanExpression exAll = exTitle.or(exContent); // 1-------------------------
    
        //조건식을 booleanBulider에 주입
        booleanBuilder.and(exAll); //2------------------------
        
        //gno 값이 0보다 크다 라는 뜻
        booleanBuilder.and(qGuestbook.gno.gt(0L));//3------------------

        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder,pageable);

        result.stream().forEach(System.out::println);
    }

    @Test
    public void testQuery3(){
        QGuestbook qGuestbook = QGuestbook.guestbook;

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression titleChk = qGuestbook.title.contains("23");
        BooleanExpression contentChk = qGuestbook.content.contains("23");
        BooleanExpression writerChk = qGuestbook.writer.contains("23");

        builder.or(titleChk).or(contentChk).or(writerChk).and(qGuestbook.gno.gt(100));

        Iterable<Guestbook> result = guestbookRepository.findAll(builder);

        result.forEach(System.out::println);




    }


}
