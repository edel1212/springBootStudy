package com.yoo.toy.dto.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User {

    private String email;
    private String password;
    private String name;
    private boolean fromSocial;

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
}
