package org.zerock.bimovie.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.bimovie.entity.Movie;
import org.zerock.bimovie.entity.Poster;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void testInsert(){
        log.info("testInsert!!!");

        // Movie 객체 생성
        Movie movie = Movie.builder()
                .title("우리집 강아지 흑곰").build();
        
        // @OneToMany 의 설정인 List<Poster>에 Poster 객체 주입
        // 1:N 설정하기 쉽게 만든 addPoster 메서드 사용
        movie.addPoster(Poster.builder().fname("흑곰이미지1.jpg").build());
        movie.addPoster(Poster.builder().fname("흑곰이미지2.jpg").build());

        // 저장
        movieRepository.save(movie);

        log.info("mno ::" + movie.getMno());

        /* 🤬 문제발생..
          - 예상과 다르게 아래의 결과 Query 와 같이  tbl_movie 데이터만 저장이 되는 문제가 발생
            movie.addPoster()로 만든 List<Poster> 데이터는 저장되지 않았음.
        Hibernate:
        insert
                into
        tbl_movie
                (moddate, regdate, title)
        values
                (?, ?, ?)

           ✅ 이유
           - 하나의 엔티티 객체에 대한 작업은 기본적으로 현재 객체에 대해서만 처리하는것이 기본이기 때문임.
             -> 현재 Test에서는 Movie 객체를 저장했기 때문에 Movie 객체만 저장되는것이고 따라서 Movie 내부의
                Poster 객체들은 영향을 받지 않는 것이다.

           ☑ 해결책
           - Move (EntityClass)의 List<Poser> 부분에 casacde 옵션을 부여해준다!
           ex) @OneToMany(...... ~~, casacde = CascadeType.ALL)
           - 옵션으로는 1) ALL : 모든 상태 변화 전파
                      2) PERSIST : 상위 엔티티 객체가 영속 컨텍스트에 저장되면 하위 엔티티들도 같이 저장
                      3) MERGE   : 햔재 객체와 영속성 컨텍스트 내 객체와 병합되면 하위 엔티티들도 같이 반영
                      4) REMOVE  : 엔티티 객체 삭제 시점에 하위 엔티티들에 전파
                      5) DETACH  : 상위 엔티티가 영속 컨텍스트에 분리되면 하위 엔티티들도 같이 분리

           -------------------------
           🎈 cascade 옵션 추가 후 결과 Query :: 두가지 전부 들어가는것을 확인 할 수있음
            Hibernate:
                insert
                into
                    tbl_movie
                    (moddate, regdate, title)
                values
                    (?, ?, ?)
            Hibernate:
                insert
                into
                    tbl_poster
                    (fname, idx, movie_mno)
                values
                    (?, ?, ?)
            Hibernate:
                insert
                into
                    tbl_poster
                    (fname, idx, movie_mno)
                values
                    (?, ?, ?)
        */
    }

    @Test
    @Transactional
    @Commit
    public void testAddPoster(){

        Optional<Movie> movie = movieRepository.findById(2L);

        if(movie.isPresent()){

            //가져온 객체에서  Poster 추가
            movie.get().addPoster(Poster.builder().fname("updateTest.jpg").build());

            movieRepository.save(movie.get());
        }//if

    }


    @Test
    @Transactional
    @Commit
    public void testRemovePoster(){
        // mno 가 2번인  movie 객체
        Optional<Movie> movie = movieRepository.findById(2L);

        if(movie.isPresent()){
            /**
             * mno가 2번인 Moive의 Poster 의 ino가 2인 포스터 삭제
             *
             * - removePoster() 내부에서는 Movie에서의 연관관계를 끊는 로직이 있고
             *   그에 따른 설정으로 Movie Entity Class 에서
             *   orphanRemoval = true 로 정의 해줬음
             * */
            movie.get().removePoster(2L);

            movieRepository.save(movie.get());
        }

    }


    @Test
    public void insertMovies(){
        IntStream.rangeClosed(10, 100).forEach(i ->{
            Movie movie = Movie.builder().title("세계명작"+ i).build();
        
            //List<Poster>에 add함
            movie.addPoster(Poster.builder().fname("세계명작"+i+"포스터.jpg").build());
            movie.addPoster(Poster.builder().fname("세계명작"+i+"포스터2.jpg").build());

            movieRepository.save(movie);
        });
    }

    
    //페이징 처리
    @Test
     public void testPaging1(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Movie> result = movieRepository.findAll(pageable);
        // Error 발생 : failed to lazily initialize a collection of role: org.zerock.bimovie.entity.Movie.posterList
        //              , could not initialize proxy - no Session
        //              org.hibernate.LazyInitializationException: failed to lazily initialize
        //              a collection of role: org.zerock.bimovie.entity.Movie.posterList
        //              , could not initialize proxy - no Session
        //
        // 원인 : m.getPosterList().size(); 코드에서 Poster에 접근할 수 없었기 떄문에 발생
        //
        // 해결방안 : EntityGraph 사용 - @See 하위 testPaing2()를 확인 하자
        result.getContent().forEach(m ->{
            log.info(m.getMno());
            log.info(m.getTitle());
            log.info(m.getPosterList().size());
            log.info("----------------------------");
        });

     }
    
     //페이징 처리 2 [EntityGraph] 사용으로  no Session 에러룰 피함
    @Test
    public void testPaging2All(){

        Pageable pageable = PageRequest.of(0,10,Sort.by("mno").descending());

        Page<Movie> result = movieRepository.findAll2(pageable);
        
        // 오류가 없지만 getPosterList()에서 가장 나중의것 을 가져오는 [첫번쨰 것이 아닌 마지막것]
        // 문제와 전체 데이터를 가져 오는등의 문제가 발생핢
        //
        // 원인 : @EntityGraph 를 이용하는 경우 전체 데이터에 대한 조인이 걸림
        //
        // 해결방안 :  양방향일 경우라도 무난한 해결첵은 @Query를 이용해는것이다.
        result.getContent().forEach(m -> {
            log.info(m.getMno());
            log.info(m.getTitle());
            log.info(m.getPosterList());
            log.info("--------------------");
        });


    }

    @Test
    public void testPaging3All(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());

        Page<Object[]> result = movieRepository.findAll3(pageable);

        result.getContent().forEach(arr ->{
            log.info(Arrays.toString(arr));
            log.info("---------------------");
        });
    }
     
}
