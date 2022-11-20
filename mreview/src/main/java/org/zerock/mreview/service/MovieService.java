package org.zerock.mreview.service;

import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.MovieImageDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.dto.PageResultDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

    Long register(MovieDTO movieDTO);

    PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO);

    /////////////////////////////////////////////////////////////////////////////////////
    //default Method                                                                    //
    /////////////////////////////////////////////////////////////////////////////////////
    /**
     * Entity -> DTO
     * */
    default MovieDTO entitiesTiDTO(Movie movie, List<MovieImage> movieImages, Double avg, Long reviewCnt){
        //Change Movie -> MovieDTO
        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .regDate(movie.getRegeDate())
                .modDate(movie.getModDate())
                .build();

        //Change List<MovieImage> -> List<MovieImageDTO>
        List<MovieImageDTO> movieImageDTOList = movieImages.stream().map(movieImage ->{
                return MovieImageDTO.builder()
                        .imgName(movieImage.getImgName())
                        .path(movieImage.getPath())
                        .uuid(movieImage.getUuid())
                        .build();
        }).collect(Collectors.toList());

        //상단에서 Change 한 List
        movieDTO.setImageDTOList(movieImageDTOList);
        //평균
        movieDTO.setAvg(avg);
        //Review Count
        movieDTO.setReviewCnt(reviewCnt.intValue());



        return movieDTO;
    }

    /**
     * DTO - > Entity 로 전환하기 위함
     * - 이전 예제들과 다른점은 MovieImage 객체들도 같이 변환해 줘야하기에
     *    <b>Map</b> 으로 타입을 지정
     * */
    default Map<String, Object> dtoToEntity(MovieDTO movieDTO){
        //반환 Map
        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();
        //Entity 로 만들어진 Movie 객체 주입
        entityMap.put("movie",movie);
    
        //햔재는 <MovieImageDTO> 
        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        if(imageDTOList != null && imageDTOList.size() > 0){
            //stream - > map()을 통해 변경
            List<MovieImage> movieImageList = imageDTOList.stream()
                    .map( movieImageDTO -> {
                        return MovieImage.builder()
                                .path(movieImageDTO.getPath())
                                .imgName(movieImageDTO.getImgName())
                                .uuid(movieImageDTO.getUuid())
                                .movie(movie) //상위에서 만들어준 Movie 객체
                                .build();
                    }).collect(Collectors.toList());
            // Entity 로 만들어진 List<MovieImage> 배열 주입
            entityMap.put("imgList", movieImageList);
        }//if

        return entityMap;
    }

    //__Eof__
}
