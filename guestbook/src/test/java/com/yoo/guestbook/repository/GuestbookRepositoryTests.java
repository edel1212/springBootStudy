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
    //TODO 이어서 단계별 분석 시작!
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



}
