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

    /***
     * @Description : 삽질내용 .. 해당  loadUser 메서드가 연결되지 않아서..
     *               삽질함 .. 분명 100 / 0 으로 Runtime Exception이 발생하지 않아
     *               왜지 .. 하고있었는데 ..
     *
     *               알고보니 oauth 를 설정하는 부분에서 scope 부분에 무지성 복붙으로 잘못된
     *               설정으로 연결이 되지 않았던거임 ...
     *
     *               근데 컴파일 에러도 안나고 로그인도 되니 에러를 발견하는데 시간이 오래 소요됨..
     *
     *               spring.security.oauth2.client.registration.google.scope=email        << ✔ 정답
     *               spring.security.oauth2.client.registration.google.client-scope=email << ❌ 오탑
     *
     *               ################
     *               security log 설정도 수정이 필요하긴했음
     *               - logging.level.org.zerock.security = debug       ## ❌
     *               - logging.level.org.zerock.club.security = debug  ## ✔
     *               실행은 되나 패키지 명을 써주자
     * */

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("-----------------------------------");
        log.info("user Request :: " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName : " + clientName);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("=================================");
        oAuth2User.getAttributes().forEach((k,v) -> {
            log.info(k + ":" + v);
        });


        return oAuth2User;

    }

}
