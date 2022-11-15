package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private List<MovieImageDTO> imageDTOList = new ArrayList<>();

}
