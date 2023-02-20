package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * 💬 Serializable(직렬화) 란 ?
 * - 자바 시스템 내부에서 사용되는 객체 또는 데이터를 외부의 자바 시스템에서도
 *   사용할 수 있도록 <strong>Byte(바이트)</strong> 형태로 데이터를 변환하는 기술이다.
 * - 시스템적으로 JVM의 Runtime Data Area(Heap 또는 Stack영역)에 상주하고 있는 객체 데이터를
 *   Byte(바이트) 형태로 변환하는 기술과 직렬화된 바이트 형태의 데이터를 객체로 변환하여 JVM으로 상주시키는
 *   형태를 말하기도한다.
 *
 * 💬 Serializable(직렬화) 사용이유 ?
 * - 서블릿 세션들은 대부분의 세션의 Java 직렬화를 지원하고 있다, 단순히 서블릿 메모리 위에서
 *   어플리케이션을 사용할 것이면 굳이 직렬화가 필요없지만, <strong>파일로 저장하거나
 *   세션 클러스트링, DB를 저장하는 옵션등을 선택하게되면 직렬화가 필요하다.</strong>
 * */
public class UploadResultDTO implements Serializable {

    private static final long serialVersionUID = 5129628467395047900L;

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
            // 💬  URLEncoder.encode() 란 ?
            //     URL에는 여러가지 규칙이 있고 그 규칙에 사용되는 문자들이 정해져있기 때문에 특정한 값들은 규칙에 맞게 변환되어야 합니다.
            //     또는 쿠키와 같이 한글을 표현하지 못하는 경우 한글을 ASCII값으로 인코딩해주야 합니다.
            return URLEncoder.encode(folderPath + File.separator + uuid+"_"+fileName , StandardCharsets.UTF_8);
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
