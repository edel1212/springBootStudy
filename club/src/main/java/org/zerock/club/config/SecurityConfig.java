package org.zerock.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Description : 해당 Class 는 시큐리티 관련 기느응ㄹ  쉽게 설정하기 위한 Class 이다
 *
 * 문제 : 현재  이전에 사용했었던 WebSecurityConfigurerAdapter class를 상속 받아 구현하려 했지만
 *            해당 Class 에 보안문제가 있어어 Deprecated 되어 사용할수 없게 됐음
 *
 *
 * 필터와 필터 체이닝
 * - 스프링 시큐리티에서 필터는 서블릿이나 jsp 에서 사용하는 필터와 같은 개념이다.
 *   ✔단! 스프링 시큐리티에서는 스프링의 빈과 연동할 수 있는 구조로 설계되어있고
 *        일반적인 필터는 스프링 빈을 사용할 수 없기 떄문에 별도의 클래스를 상속 받는 형태가 많다.
 *        
 *  - 스프링 시큐리티에서 가장 핵심적인 기능을 하는 필터는 <b>AuthenticationManager</b>이다
 *     - 해당 필터가 가진 인증처리 메서드는 파라미터도 Authentication 타입이고 반환 타입 또한
 *       Authentication 이다
 * */
@Configuration
@Log4j2
public class SecurityConfig{

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    /**
     * 설정을 통한 권한에 따른 URL 접근 제한
     * - throws Exception 필수이다
     * */
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{
        
        
        //antMatchers("???") 의 URL 은 **/* 와같은 앤트 스타일 패턴으로 자원을 선택도 가능함 
        httpSecurity.authorizeHttpRequests((auth) ->{
            auth.antMatchers("/sample/all").permitAll();     // 누구나 로그인 없이도 /sample/all 에 접근 가능
            auth.antMatchers("/sample/member").hasRole("USER");  //User 권한을 갖으면 /sample/member 에 접근 가능
        });

        httpSecurity.formLogin();
        httpSecurity.csrf().disable();
        httpSecurity.logout();

        return httpSecurity.build();
    }

    /**
     * 임시 계정 생성
     * */
    @Bean
    protected InMemoryUserDetailsManager userDetailService(){
        UserDetails user = User.builder()
                .username("user1")                                      //ID
                .password(passwordEncoder().encode("1111"))  // PW - Security 는 encoding 된 PW를 꼭 사용해아함
                .roles("USER")                                          //권한
                .build();
        log.info("userDetailsService!!!!");
        log.info(user);
        //log :: org.springframework.security.core.userdetails.User [Username=user1, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, credentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_USER]]
        return new InMemoryUserDetailsManager(user);
    }

}
