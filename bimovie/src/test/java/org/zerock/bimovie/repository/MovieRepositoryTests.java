package org.zerock.bimovie.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.bimovie.entity.Movie;
import org.zerock.bimovie.entity.Poster;

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

}
