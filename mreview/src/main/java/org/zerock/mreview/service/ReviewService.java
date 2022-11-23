package org.zerock.mreview.service;

import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewService {

    //모든 영화의 리뷰를 가져온다
    List<ReviewDTO> getListOfMovie(Long mno);

    //리뷰 추가
    Long register(ReviewDTO movieReview);

    //리뷰 수정
    void modify(ReviewDTO movieReviewDTO);

    //리뷰 삭제
    void remove(Long reviewnum);

    //DTO -> Entity
    default Review dtoTOEntity(ReviewDTO movieReviewDTO){
        return Review.builder()
                .reviewnum(movieReviewDTO.getReviewnum())
                .movie(Movie.builder().mno(movieReviewDTO.getMno()).build())
                .member(Member.builder().mid(movieReviewDTO.getMid()).build())
                .grade(movieReviewDTO.getGrade())
                .text(movieReviewDTO.getText())
                .build();
    }

    //Entity -> DTO
    default ReviewDTO entityToDTO(Review review){
        return ReviewDTO.builder()
                .reviewnum(review.getReviewnum())
                .mno(review.getMovie().getMno())
                .mid(review.getMember().getMid())
                .nickname(review.getMember().getNickname())
                .email(review.getMember().getEmail())
                .grade(review.getGrade())
                .text(review.getText())
                .regDate(review.getRegeDate())
                .modDate(review.getModDate())
                .build();
    }

}
