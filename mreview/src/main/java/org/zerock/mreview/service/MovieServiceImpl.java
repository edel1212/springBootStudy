package org.zerock.mreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.dto.PageResultDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.repository.MovieImageRepository;
import org.zerock.mreview.repository.MovieRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    private final MovieImageRepository movieImageRepository;

    @Transactional
    @Override
    public Long register(MovieDTO movieDTO) {
        
        // 받아온 DTO - > Entity 로 변환
        Map<String, Object> entityMap = dtoToEntity(movieDTO);
        
        Movie movie = (Movie) entityMap.get("movie");
        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        movieRepository.save(movie);

        movieImageList.forEach(movieImageRepository::save);

        return movie.getMno();
    }

    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());

        Page<Object[]> result = movieRepository.getListPage(pageable);

        Function<Object[], MovieDTO> fn = arr -> this.entitiesTiDTO(
                                                        (Movie) arr[0]
                                                        , (List<MovieImage>) List.of((MovieImage) arr[1])
                                                        , (Double) arr[2]
                                                        , (Long) arr[3]
                                                );

        return new PageResultDTO<>(result ,fn);
    }

    @Override
    public MovieDTO getMovie(Long mno) {
        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        Movie movie = (Movie) result.get(0)[0]; //Movie Entity 는 가장 앞에 존재 - result의 모든 Row 가 동일값

        List<MovieImage> movieImageList = new ArrayList<>(); //result에서 받아온 데이터를  넣을 Array
        result.forEach(arr->{
            MovieImage movieImage = (MovieImage)arr[1]; //result.get(index)번쨰의 [1] 데이터를 주입
            movieImageList.add(movieImage);
        });
        
        Double avg = (Double) result.get(0)[2]; //평균 평점 - result의 모든 Row 값은 동일한 값임

        Long reviewCnt = (Long) result.get(0)[3];//리뷰 개수 - result의 모든 Row 값은 동일한 값임

        return this.entitiesTiDTO(movie, movieImageList, avg, reviewCnt);
    }
}
