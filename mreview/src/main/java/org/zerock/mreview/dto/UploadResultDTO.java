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
     * π νλ¨ getter Methodλ
     *   - ImageURL
     *   - ThumbnailURL
     *   λΌλ 2κ°μ λ³μκ° μμ§λ§ ν΄λΉ classμ κ°μ²΄ μμ± μ
     *   μλμΌλ‘ μΆκ°λλ€.
     *
     *  β λ¨ λ©μλ λͺμ get λΆλΆμ λ°κΏ μ μμ± X getter λ‘ μΈμνμ§ λͺ»ν΄μμ!
     * **/

    /**
     * Full Path λ₯Ό κ°μ Έμ¬λ μ¬μ©νκΈ° μν¨
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
