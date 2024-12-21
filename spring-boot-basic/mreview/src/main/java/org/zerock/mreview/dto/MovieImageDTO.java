package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * @Description : 파일에 관련된 정보를 작성함
 * */
public class MovieImageDTO {

    private String uuid;

    private String imgName;

    private String path;

    public String getImageURL(){
        try {
            return URLEncoder.encode(path+"/"+uuid+"_"+imgName, StandardCharsets.UTF_8);
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
            return URLEncoder.encode(path+"/" + "s_" +uuid+"_"+imgName, StandardCharsets.UTF_8);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    
    
}
