package org.zerock.club.security.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * @Description : 해당 Class 는 UserDetailsService(Interface) 의 OAuth2 버전으로 생각하면 된다
 *                => DefaultOAuth2UserService 를 상속받아 사용
 *                - OAuth 의 인정 결과를 처리하는 Class
 *
 * ✅  UserDetailsService :: Interface
 *     DefaultOAuth2UserService :: class
 * */
@Service
@Log4j2
public class ClubOAuth2USerDetailsService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("-----------------------------------");
        log.info("user Request :: " + userRequest);

        return super.loadUser(userRequest);

    }

}
