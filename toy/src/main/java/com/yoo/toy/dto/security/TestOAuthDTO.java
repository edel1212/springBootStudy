package com.yoo.toy.dto.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


/*******
 *
 * 해당 Class는 userDtails를 구현하지 않고 OAuth2User만으로 구현된
 * DTO이다 해당 class로 OAuth로그인 자체는 문제없이 가능하지만
 *
 * 👉 Conroller에서 @PreAuthorize(권한) 자체를 인식하지 못하는것으로
 *    미루어 보아 시큐리티 자체는 userDetails를 사용하여 인가 정보를 확인하니
 *    생각한데로 둘다 구현하여 super()를 통해 생성자 주입을 해주어야한다!
 *
 * ******/
@Log4j2
@Getter
@Setter
@ToString
public class TestOAuthDTO  implements OAuth2User {



    private String email;
    private String password;
    private String name;
    private boolean fromSocial;

    // OAuth2User에서 필요로함
    private Map<String, Object> attrs;

    private GrantedAuthority grantedAuthority;

    public TestOAuthDTO(
            String username
            , String password
            , boolean fromSocial
            , Collection<? extends GrantedAuthority> authorities
            ,Map<String, Object> attrs) {
        this.email = username;
        this.password = password;
        this.name = username;
        this.fromSocial = fromSocial;
        // 추가된 매게변수
        this.attrs = attrs;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


}
