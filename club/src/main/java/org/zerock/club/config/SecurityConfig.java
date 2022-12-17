package org.zerock.club.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.club.security.filter.ApiCheckFilter;
import org.zerock.club.security.handler.ClubLoginSuccessHandler;
import org.zerock.club.security.service.ClubOAuth2USerDetailsService;
import org.zerock.club.security.service.ClubUserDetailService;

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
 *       
 *  
 *  -------------------------------------------------------------------------------------
 *   
 * @Desceiptoin : Annotation 으로 Url 별 권한 메칭 방법 
 *               - @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) 추가
 *               - 원하는 권한의 Controller Method 에 @PreAuthorize 추가
 *                  -> ex) @PreAuthorize("hasRole('추가')")
 * */
@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) //어노테이션으로 url 권한 설정하기 위함.
public class SecurityConfig{

    @Autowired
    private ClubUserDetailService clubUSerDetailService;


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
        
        /***---------------------*---------------------*---------------------*/
        /** url Matching  방법  [ EnableGlobalMethodSecurity 사용으로 주석처리 ] */
        /***---------------------*---------------------*---------------------*/
        //antMatchers("???") 의 URL 은 **/* 와같은 앤트 스타일 패턴으로 자원을 선택도 가능함
//        httpSecurity.authorizeHttpRequests((auth) ->{
//            auth.antMatchers("/sample/all").permitAll();     // 누구나 로그인 없이도 /sample/all 에 접근 가능
//            auth.antMatchers("/sample/member").hasRole("USER");  //User 권한을 갖으면 /sample/member 에 접근 가능
//        });
//---------------------------------------------------------------------------------------------------------


        /***
         * 1)  login form
         * - formLogin() 추가 시 :: 인가 , 인증에 문제시 자동으로 로그인 화면으로 이동시켜줌
         *
         * ❌ 단점은 해당 매서드를 이용하는 경우에는 별도의 디자인을 적용 불가능한 Spring Security 에서 제공하는 UI를 사용해야함
         * ✔ loginPage() 혹은 loginProcessUrl(), defaultSuccessUrl(), failureUrl() 등을 이용하면
         *    필요한 설정을 지정할 수있다.
         *    - 대부분의 어플리게이션은 고유의 디자인을 적용하기 떄문에 loginPage()를 이용해 별도의 페이지를 만들어 사용!
         *    
         * 2) logout form
         * - logout() 추가 시 :: 로그아웃 페이지 생성
         * ❌ 단점은 fromLogin 과 같이 별도 디자인 적용 가능
         * ✔ logoutUrl(), logoutSuccessUrl() 등으로  커스텀 페이지 제작 가능
         *   , invalidateHttpSession() , deleteCookies() 를 추가해 세션 , 쿠기도 삭제 가능
         * */
        httpSecurity.formLogin();       // 권한이 없는 페이지 -> 로그인 페이지 이동
        httpSecurity.csrf().disable();  // csrf 사용  X
        httpSecurity.logout();          // 로그아웃 페이지 생성  URL : host/logout

        //Google social Login 추가
        httpSecurity.oauth2Login()
                .successHandler(successHandler());  //하위에 만들어준 Method Call
        
        //RememberMe 기능 추가
        httpSecurity.rememberMe()
                .tokenValiditySeconds(60*60*24*7)  //cookie 유지 기간 -- 7일
                .userDetailsService(clubUSerDetailService); // userDetailService 추가 필요 - Spring Bean 주입 !


        /////////////////////////////////////////////////////////
        // Filter 순서 변경
        // - UsernamePasswordAuthenticationFilter 필터 이전에 실행되게 끔 설정
        // - 해당 UsernamePasswordAuthenticationFilter 는 사용자의
        //   아이디와 패스워드를 기반으로 동작하는 필터이다!.
        // - 그러므로 인가 받은 사용자만 사용하게끔 한것임!
        // - 👿 해당 apiCheckFilter() 내 소스를 보면 match 가될떄 return 을 시키므로
        //   API 를 Call 해도 response 해주느 Data가 없는것을 확인할 수있음!
        /////////////////////////////////////////////////////////
        httpSecurity.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /**
     * @Description : AuthenticationSuccessHandler 를 구현한 Class
     *               - Bean Annotation 을 퉁해 Spring Container 에 주입해준다.
     *               ✔ 로그인 성공 시 진행될 로직을 구현해놓은 Class 를 반환한다.
     * */
    @Bean
    public ClubLoginSuccessHandler successHandler(){
        return new ClubLoginSuccessHandler( passwordEncoder() );
    }

    /**
     * 임시 계정 생성
     * */
//    @Bean
//    protected InMemoryUserDetailsManager userDetailService(){
//        UserDetails user = User.builder()
//                .username("user1")                                      //ID
//                .password(passwordEncoder().encode("1111"))  // PW - Security 는 encoding 된 PW를 꼭 사용해아함
//                .roles("USER")                                          //권한
//                .build();
//        log.info("userDetailsService!!!!");
//        log.info(user);
//        //log :: org.springframework.security.core.userdetails.User [Username=user1, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, credentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_USER]]
//        return new InMemoryUserDetailsManager(user);
//    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
// Api Filter 적용
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Bean
    public ApiCheckFilter apiCheckFilter(){
        return  new ApiCheckFilter("/notes/**/*"); // Ant 형식으로 Filter를 할 URL 추가
    }
    
    //__Eof__
}
