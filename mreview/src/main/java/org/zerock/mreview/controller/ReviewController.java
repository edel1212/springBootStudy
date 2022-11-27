package org.zerock.mreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.service.ReviewService;

import java.util.List;

@Controller
@RestController
@Log4j2
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{mno}/all")
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("mno") Long mno){

        log.info("-------------list----------------");
        log.info("mno ::: " + mno);

        List<ReviewDTO> reviewDTOList = reviewService.getListOfMovie(mno);

        return new ResponseEntity<>(reviewDTOList,HttpStatus.OK);
    }


    @PostMapping("/{mno}")
    public ResponseEntity<Long> addReview(@RequestBody ReviewDTO movieReviewDTO){
        log.info("-------------add MovieReview--------------");
        log.info("reviewDTO ::: " + movieReviewDTO);

        Long reviewNum = reviewService.register(movieReviewDTO);
        return new ResponseEntity<>(reviewNum,HttpStatus.OK);
    }

    @PutMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> modifyReview(@PathVariable("reviewnum") Long reviewnum
            ,@RequestBody ReviewDTO movieReviewDTO){
        log.info("----------------modify MovieReview---------------");

        log.info("reviewDTO ::: " + movieReviewDTO);

        reviewService.modify(movieReviewDTO);

        return  new ResponseEntity<>(reviewnum, HttpStatus.OK);

    }

    @DeleteMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> removeReview(@PathVariable("reviewnum") Long reviewnum){
        log.info("------------modify removeReview-----------");
        reviewService.remove(reviewnum);

        return new ResponseEntity<>(reviewnum,HttpStatus.OK);
    }

}
