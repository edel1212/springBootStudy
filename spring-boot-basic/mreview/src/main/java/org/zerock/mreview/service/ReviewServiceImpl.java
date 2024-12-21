package org.zerock.mreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;
import org.zerock.mreview.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewDTO> getListOfMovie(Long mno) {
        Movie movie = Movie.builder().mno(mno).build();
        
        //Query Method 사용
        List<Review> result = reviewRepository.findByMovie(movie);

        return result.stream().map((this::entityToDTO)).collect(Collectors.toList());
    }

    @Override
    public Long register(ReviewDTO movieReview) {
        Review review = dtoTOEntity(movieReview);
        
        //JapRepository 내부 메서드 사용
        reviewRepository.save(review);

        return review.getReviewnum();
    }

    @Override
    public void modify(ReviewDTO movieReviewDTO) {
        
        //Optional 을 이용하여 유무 체크
        Optional<Review> result =
                reviewRepository.findById(movieReviewDTO.getReviewnum());

        if(result.isPresent()){
            Review movieReview = result.get();
            //getter()를 이용해 Entity 값을 변경 후 save 해준다.
            movieReview.changeGrade(movieReviewDTO.getGrade());
            movieReview.changeText(movieReviewDTO.getText());
            reviewRepository.save(movieReview);
        }

    }

    @Override
    public void remove(Long reviewnum) {
        reviewRepository.deleteById(reviewnum);
    }
}
