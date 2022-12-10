package org.zerock.club.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * @Description : Security 에서는 <strong>username 을 Id 로 쓰고있어 햇갈릴 수도 있고 </strong>추가로
 *                기존 Security 의 계정 정보에는 없는 정보가 내가 사용하려는 어플리케이션
 *                계정정보에서 필요할수도 있기에 DTO Class 를 이용하여 추가 및 변환 해줌
 *                
 *                - 이전 예제에서 했던 DTO와 같이 포장 개념으로 생가하면 쉬움
 *
 *-------------------------------------------------------------------------
 *  
 *  🎈 <<Social Login>> 로그인 성공 시 Controller 에서 ClubAuthMemberDTO 로 받는데
 *                     일반 로그인의 경우 문제가 없지만 Social Login 시 에는 
 *                     해당 값이 null 로 들어오는 문제가 있음
 *                     
 *                     따라서 OAuth2User가 Interface로 되어있기 떄문에 해당
 *                     Interface를 implements 해주자
 *                     
 *                     ✔ 해당 class 의 새엇ㅇ자에 social 값을 가져올수 있도록
 *                       Map<String, Object> 형식의 파라미터를 추가해주자
 *  
 * */
@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User implements OAuth2User {

    private String email;

    private String password;

    private String name;

    private boolean fromSocial;
    
    private Map<String, Object> attr; //Social Login 시 정보를 담음

    /*********************************/
    /**Social Login Version Constructor*/
    /*********************************/
    public ClubAuthMemberDTO(
            String username        //ID
            , String password       //PW
            , boolean fromSocial     //소설 유,무  ::: 추가된것
            , Collection<? extends GrantedAuthority> authorities
            , Map<String, Object> attr) { //Collection Type 으로 권한이 있음
        //하위 생성자를 사용
        this(username, password, fromSocial, authorities);
        this.attr = attr;
    }


    public ClubAuthMemberDTO(String username            //ID
                            , String password           //PW
                            , boolean fromSocial        //소설 유,무  ::: 추가된것
                            , Collection<? extends GrantedAuthority> authorities) { //Collection Type 으로 권한이 있음
        // Security 의 부모 Class (User)에서 필요로 하는것은 ID, PW , 권한목록이기에 해당 값을 전달해줘서 
        // 계정 유무 확인
        super(username, password, authorities);
        //이후 내가 필요한 값들을 지정
        this.email      = username;
        this.password   = password;
        this.fromSocial = fromSocial;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }
}
