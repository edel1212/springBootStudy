package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadResultDTO implements Serializable {

    private String fileName;
    private String uuid;
    private String folderPath;


    /***
     * 👍 하단 getter Method는
     *   - ImageURL
     *   - ThumbnailURL
     *   라는 2개의 변수가 없지만 해당 class의 객체 생성 시
     *   자동으로 추가된다.
     *
     *  ✔ 단 메서드 명의 get 부분을 바꿀 시 생성 X getter 로 인식하지 못해서임!
     * **/

    /**
     * Full Path 를 가져올떄 사용하기 위함
     * */
    public String getImageURL(){
        try {
            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName, StandardCharsets.UTF_8);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Thumbnail Full Path
     * */
    public String getThumbnailURL(){
        try {
            return URLEncoder.encode(folderPath+"/" + "s_" +uuid+"_"+fileName, StandardCharsets.UTF_8);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
