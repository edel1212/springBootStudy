package org.zerock.mreview.Repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

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

}
