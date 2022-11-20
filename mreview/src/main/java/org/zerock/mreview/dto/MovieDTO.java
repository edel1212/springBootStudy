package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * @Diescription : - 해당 Class 는 Register UI 에서 넘겨주는 값을 기준으로 작성
 *                 - Upload 된 File을 위한 DTO도 추가생성 :: multipart Type 이므로 배열형식으로!
 * */
public class MovieDTO {

    private Long mno;

    private String title;

    @Builder.Default  // 해당 어노테이션은 객체 생성 시 원하는 값으로 초기화해서 반환 받을 수 있다.
    private List<MovieImageDTO> imageDTOList = new ArrayList<>();
    
    //영화의 평균 평점
    private double avg;

    //리뷰 수 jpa 의 count();
    private int reviewCnt;
    
    //생성 , 수정일
    private LocalDateTime regDate, modDate;


}
