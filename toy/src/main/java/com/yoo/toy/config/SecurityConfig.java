package com.yoo.toy.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration //BeanContainer에서 해당 Class를 스캔하도록 지정
@Log4j2
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

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{

        /**
         * httpSecurity.authorizeRequests()를 사용하여 인증이 필요한 자원들을 설정할 수 있다.
         * */
        httpSecurity.authorizeRequests()
                /**
                 * antMatchers("targetUrl"); <<- 엔트 패턴으로도 지정이 가능하다
                 * */
                // 누구나 로그인 없이도 /sample/all 에 접근 가능
                .antMatchers("/sample/all").permitAll()
                //User 권한을 갖으면 /sample/member 에 접근 가능
                .antMatchers("/sample/member").hasRole("USER")

                .and()      //추가적 옵션을 달수있다.

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
                .formLogin();

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
