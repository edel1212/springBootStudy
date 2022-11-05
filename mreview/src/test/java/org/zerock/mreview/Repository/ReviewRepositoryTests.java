package org.zerock.mreview.Repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void randomTest(){
        IntStream.rangeClosed(1, 200).forEach(i ->{
            Long mno =  (long) (Math.random() * 101) + 100;
            log.info(mno);
        });

    }

    @Test
    public void insertMovieReviews(){

        //200개의 리뷰를 등록
        IntStream.rangeClosed(1, 200).forEach(i ->{
            
            //영화번호 - DB상에 존재해야함
            Long mno =  (long) (Math.random() * 99) + 100;
            
            //리뷰어 번호 --DB상에 존재해야함
            Long mid =  (long) (Math.random() * 99) + 1;
            Member member = Member.builder().mid(mid).build();

            Review review = Review.builder()
                    .member(member)
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int) (Math.random()*5)+1 )
                    .text("영화 감상평 :::" + i)
                    .build();

            reviewRepository.save(review);

        });
        
    }

    @Test
    public void testGetMovieReviews(){
        Movie movie = Movie.builder().mno(120L).build();

        List<Review> result = reviewRepository.findByMovie(movie);

        result.forEach(data ->{
            log.info(data.getReviewnum());
            log.info("--------------------");
            log.info(data.getGrade());
            log.info("--------------------");
            log.info(data.getText());
            log.info("--------------------");
            /**
             * 일반적인 methodName Query 를 사용해서 해당 member 의 email 값을 가져오려하면 Error 발생
             * -[Error Message] no Session
             * 이유❔
             * - Review Class 의 Member 에 대한 Fetch 방식이 Lazy 이기 때문에
             *   한 번에 Review 객체와 Member 객체를 조회할 수 없기 떄문임
             *   
             * ✔ 해결방법
             *   - 1) @Transactional 을 사용한다 :: 이것은 해당 getMember().getEmail()을 처리할떄마다 조회를
             *                                    다시 한다는 문제가 있음
             *   - 2) @Query() JPQL 을 사용하여 Join 을 사용한다
             *   
             *   - 3) @EntityGraph 을 이용해서 ㅂReview 객체를 가져올때마다  Member 객체를 로딩 시킨다
             *   //TODO 이어서 작성 필요
             *
             * **/
            log.info(data.getMember().getEmail());
            log.info("--------------------");
        });

    }

}
