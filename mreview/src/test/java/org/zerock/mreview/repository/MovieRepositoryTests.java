package org.zerock.mreview.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieImageRepository movieImageRepository;

    @Test
    @Transactional
    @Commit //테스트가 완료된 후에도 DB에 데이터를 남아 있게 하기 위함
    public void insertMovies(){
        /**
         * 해당 테스트에서 중요한점은 해당 Movie 가 저장될때 MovieImage 도 같이
         * 저장되게끔 해줘야하는 것이다 이유는 MovieImage 에서 Moive를 FK 로 잡고있음!!
         * */
        IntStream.rangeClosed(1,100).forEach(i->{
            Movie movie = Movie.builder()
                    .title("Movie..." + i)
                    .build();
            log.info("----------------------------------");
            movieRepository.save(movie);

            int count = (int) (Math.random()*5) +1;

            for(int j = 0 ; j <count ; j++){
                MovieImage movieImage = MovieImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .imgName("test"+j+"jpg")
                        .build();
                movieImageRepository.save(movieImage);
            }

            log.info("----------------------------------");

        });
    }

    @Test
    public void testListPage(){

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC,"mno"));

        Page<Object[]> result = movieRepository.getListPage(pageRequest);

        for(Object[] obj : result.getContent()){
            log.info(Arrays.toString(obj));
        }
    }

    @Test
    public void testListPageInumDesc(){
        PageRequest pageRequest = PageRequest.of(0,10,Sort.by("mno").descending());
        Page<Object[]> result = movieRepository.getListPageOrdeyByInum(pageRequest);
        result.getContent().stream().map(Arrays::toString).forEach(log::info);
    }


    @Test
    public void testGetMovieWithAll(){
        List<Object[]> result = movieRepository.getMovieWithAll(150L);

        log.info(result);

        result.stream().map(Arrays::toString).forEach(log::info);

    }

}
