package com.yoo.toy.dto.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User implements OAuth2User {

    private String email;
    private String password;
    private String name;
    private boolean fromSocial;

    // OAuth2User에서 필요로함
    private Map<String, Object> attrs;



    /**
     * 일반 로그인
     * - User를 상속을 받았기에  super(username, password, authorities);
     *   생성자 메서드를 사용해줘야한다.
     * */
    public ClubAuthMemberDTO(
                        String username
                        , String password
                        , boolean fromSocial
                        , Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.email = username;
        this.fromSocial = fromSocial;
        /**
         * ☠️ password 주입하지 않아 삽질함...
         * Error Msg : empty encode password error...
         *
         * 나의 생각은 DTO를 만들때 UserClass에서 알아서 확인해주므로
         * password 주입이 필요없다 생각했는데 아니였다 ..
         * Security Config에서 설정한 AuthenticationManager 부분의 설정중에
         * Service의 PW를 읽는 부분이 있는데 해당 부분을 먼저 거친다음 가기 떄문에
         * this.password = password 지정이 필요하다!!
         * */
        this.password = password;
    }


    /**
     * 소셜 로그인
     * - OAuth2User를 implements 했을 경우
     *   강제하는 메서드인 getAttributes() 를 생성메서드에 추가해줌
     *
     * 👉 User 와 OAuth2User 차이점
     *    - OAuth2User는 Map 타입으로 Key : attributes 에
     *      모든 인증결과를 갖고 있기에  @Override Method인 getAttributes()를
     *      사용하여 인증값을 주입해 주어야한다.
     * */
    public ClubAuthMemberDTO(
            String username
            , String password
            , boolean fromSocial
            , Collection<? extends GrantedAuthority> authorities
            ,Map<String, Object> attrs) {
        this(username, password, fromSocial, authorities);
        // 추가된 매게변수
        this.attrs = attrs;
    }


    // OAuth2User에서 필요로함
    @Override
    public Map<String, Object> getAttributes() {
        return this.attrs;
    }
}
