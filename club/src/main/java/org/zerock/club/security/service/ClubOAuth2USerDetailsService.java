package org.zerock.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.ClubMemberRole;
import org.zerock.club.repository.ClubMemberRepository;

import java.util.Optional;

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
@RequiredArgsConstructor
public class ClubOAuth2USerDetailsService extends DefaultOAuth2UserService {

    private final ClubMemberRepository repository;

    private final PasswordEncoder passwordEncoder;

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
     *
     *
     *               ✔ 해당 Method 는  OAuth Login 시 에만 탄다!✔
     *              
     *               ✔ 해당 Method 는  Google 로그인이 성공하지 않으면 타지 않음!
     *                 따라서 해당 메스드에 탔다는건 구글 인증이 되었다는것임
     * */   

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("-----------------------------------");
        log.info("user Request :: " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName : " + clientName); //사용하는 Client Api 명 [Google]
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("=================================");
        oAuth2User.getAttributes().forEach((k,v) -> {
            //sub, picture, email, email_verified ... 출력
            log.info(k + ":" + v);
        });

        String email = null;
    
        //구글일경우 oAuth 객체헤서 email 정보를 가져옴
        if(clientName.equals("Google")) email = oAuth2User.getAttribute("email");

        log.info("email::: " + email);

        ClubMember member = saveSocialMember(email);

        return oAuth2User;

    }


    /**
     * @Descripton : Social Login 시 받아온 정보를 토대로 회원 정보를
     *               DB에 저장하는 Method
     * */
    private ClubMember saveSocialMember(String email){

        //기존에 동일한 이메일로 가입한 회원이 있는지 조회
        Optional<ClubMember> result = repository.findByEmail(email,true);// Email , Social  유무

        //이미 가입한 회원일경우 return
        if(result.isPresent()) return result.get();

        /**
         * 없다면 회원 추가 다만 여기서 문제는 비밀번호는 어떤걸로 해줘야하나 ..고민해봐야함
         * 
         * */
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(email)
                .password(passwordEncoder.encode("1111")) // Spring Security 는 Pw Encoding 필수
                .fromSocial(true)
                .build();
    
        //권한 추가 -- USER
        clubMember.addMemberRole(ClubMemberRole.USER);
    
        //DB 등록
        repository.save(clubMember);

        return clubMember;

    }

    //__Eof__
}
