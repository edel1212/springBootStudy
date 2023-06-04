package com.yoo.loginServer.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;


@Getter
@Setter
@Log4j2
public class MemberAuthDTO extends User {

    private String id;
    private String pw;
    private String name;

    private String role;


    //////////////

    // OAuth2로그인 성공 시 모든 정보를 갖고있음
    private Map<String, Object> attrs;

    // UserDetailsService 사용
    public MemberAuthDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = username;
        this.pw = password;
    }

}
