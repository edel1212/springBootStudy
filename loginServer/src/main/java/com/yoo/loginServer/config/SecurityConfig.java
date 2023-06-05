package com.yoo.loginServer.config;

import com.yoo.loginServer.security.filter.ApiCheckFilter;
import com.yoo.loginServer.security.handler.AuthFailureHandler;
import com.yoo.loginServer.security.handler.AuthSuccessHandler;
import com.yoo.loginServer.security.service.MemberDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberDetailService memberDetailsService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public ApiCheckFilter apiCheckFilter(){
        //URI 패턴 추가
        return new ApiCheckFilter("/notes/**/*");
    }

    public AuthFailureHandler authFailureHandler(){
        return new AuthFailureHandler();
    }

    public AuthSuccessHandler authSuccessHandler(){
        return new AuthSuccessHandler();
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{

        AuthenticationManager authenticationManager = httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(memberDetailsService)
                .passwordEncoder(this.passwordEncoder())
                .and().build();

        httpSecurity.authenticationManager(authenticationManager);

        httpSecurity
                .formLogin()
                .loginProcessingUrl("/user/login")
                .successHandler(this.authSuccessHandler())
                .failureHandler(this.authFailureHandler());

        httpSecurity.csrf().disable();


        // JWT를 사용할 것이며 서버가 나눠져 있으므로 해제
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        // 인증 처리전 확인
        httpSecurity.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
