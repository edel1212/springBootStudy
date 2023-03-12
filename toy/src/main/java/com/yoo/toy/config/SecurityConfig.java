package com.yoo.toy.config;

import com.yoo.toy.service.securiry.ClubUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@Configuration //BeanContainer에서 해당 Class를 스캔하도록 지정
@Log4j2
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    /**
     * Spring Boot 2.0 이상의 버전에서 부터
     * Security 인증에는 반드시 PasswordEncoder를 필요로 하기에
     * 해당 객체를 Bean에 등록해줘야한다!
     * */
    @Bean
    PasswordEncoder passwordEncoder(){
        // bcrypt는 해시 함수를 이용하여 암호화하는
        // 단반향 암호화 Class이다.
        return new BCryptPasswordEncoder();
    }

    /**
     * 성공 시 Handler를 주입해줌
     * */
    @Bean
    public CustomAuthSuccessHandler customAuthSuccessHandler(){
        return new CustomAuthSuccessHandler(passwordEncoder());
    }

    /**
     * 변경하고싶은 로직을 작성한 Class인 UserDetailsService를 구현한
     * ClubUserDetailsService를 주입하여 사용함
     * */
    @Autowired
    private ClubUserDetailsService clubUSerDetailService;

    //Login Fail Handler
    @Autowired
    private AuthenticationFailureHandler customAuthFailureHandler;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{

        /***
         * @Description : Spring-boot 의 버전이 올라가면서 authenticationManger() 주입법이 바뀜.
         *               - 이전에는 해당 Class에 상속관계인 WebSecurityConfigurerAdapter 에서
         *                 구현된 메서드라 따로 수정없이 사용이 가능했지만 현재는 deprecated 되어서
         *                 👉 따로 ClubUSerDetailsService를 주입 받아 AuthenticationManager 객체를
         *                    생성해줘야한다.
         * */
        AuthenticationManager authenticationManager = httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(clubUSerDetailService)
                .passwordEncoder(this.passwordEncoder())
                .and()
                .build();
        httpSecurity.authenticationManager(authenticationManager);


        /***
         *  formLogin() 이란 ?
         *  - 인가, 인증에 문제가 생길시 로그인 페이지로 이동하끔 함
         *
         * ❌ formLogin()의 단점은 해당 매서드를 이용하는 경우에는 별도의 디자인을 적용 불가능한 Spring Security에서
         *    제공하는 UI를 사용해야함
         *
         * 👉 로그인 화면 , 성공 실패 등 커스텀 페이지 생성 후 URL을 지정하고 싶다면 ?
         *    loginPage()
         *    , loginProcessUrl()
         *    , defaultSuccessUrl()
         *    , failureUrl()
         *     등을 이용하면 필요한 설정을 지정할 수있다.
         *    - 대부분의 어플리게이션은 고유의 디자인을 적용하기 떄문에 loginPage()를 이용해 별도의 페이지를 만들어 사용!
         **/
        httpSecurity.formLogin()
                .loginPage("/sample/login")                 // Login Page URL  [GET]
                .loginProcessingUrl("/sample/loginProcess") // 로그인 Request URL [POST]
                .failureHandler(customAuthFailureHandler)   // 실패 시 처리 Handler 지정
                .successHandler(customAuthSuccessHandler());  // 로그인 성공 시 처리 Handler 지정

        httpSecurity.oauth2Login();


        // CSRF를 사용하지 않도록 설정
        httpSecurity.csrf().disable();

        /**
        *   Logout 설정
        * -  로그아웃 페이지 생성
        * ❌ logout()의 단점은 fromLogin 과 같이 별도 디자인 적용 가능
         *
        *  👉 커스텀 페이지를 사용하려면  ?
         *  logoutUrl()
         *  , logoutSuccessUrl()
         *   등으로  커스텀 페이지 제작 가능
         *
        *   👉 로그아웃 시 쿠키 및 세션 삭제
         *   invalidateHttpSession(Boolean)
         *   , deleteCookies()
         *   를 사용하여 로그웃시 쿠키및 세션을 무효하 설정도 가능하다
         *   ex) httpSecurity.logout().deleteCookies().invalidateHttpSession(true);
         *
        * */
        httpSecurity.logout();

        return httpSecurity.build();
    }



}
