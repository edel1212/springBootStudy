package com.yoo.toy.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.yoo.toy.entity.GuestBook;
import com.yoo.toy.entity.QGuestBook;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class GuestBookRepositoryTests {

    @Autowired
    private GuestBookRepository guestBookRepository;


    @Test
    public void dummyDateInsertTest(){
        IntStream.rangeClosed(1, 100).forEach(i ->{
            GuestBook guestBook = GuestBook.builder()
                    .title("Title . . ." + i)
                    .content("Content .. ." + i)
                    .writer("user"+ (i % 10))
                    .build();
            guestBookRepository.save(guestBook);
        });
    }

    ////////////////////////////////////////////////////////

    //Qurydsl 테스트 시장

    /**
     * title에 "1"이 들어가있는 0페이지 ~ 10개 gnum으로 정렬 된 데이터
     */
    @Test
    public void testQuery1(){
        //Pageable 객체 생성 0페이지 10개 gnum Desc 정렬
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gnum"));
        
        QGuestBook qGuestBook =  QGuestBook.guestBook; // Q-guestBook 생성

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression expression = qGuestBook.title.contains(keyword); // 조건식 추가
        
        builder.and(expression); // 조건 And 조건으로 추가

        Page<GuestBook> result = guestBookRepository.findAll(builder,pageable); // Q도메인의 predicate 와 pageable을 주입

        result.getContent().forEach(log::info);

        
    }

    //제목 혹은 내용에 "1"이들어가고 gnum이 30보다 큰
    // 0페이지 10개의 목록 gnum Desc 정렬
    @Test
    public void testQuery2(){
        Pageable pageable =  PageRequest.of(0, 10,Sort.by("gnum").descending());
        String keyword = "1";
        QGuestBook qGuestBook = QGuestBook.guestBook; // Q 도메인 생성

        //조건식
        BooleanExpression booleanExpression = qGuestBook.title.contains(keyword)
                .or(qGuestBook.content.contains(keyword))
                .and(qGuestBook.gnum.goe(0)); //객체 생성

        // 조건식을 담을 객체
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(booleanExpression);
        
        Page<GuestBook> result = guestBookRepository.findAll(builder, pageable);

        result.getContent().forEach(log::info);

    }

    //__Eof__
}
