package org.zerock.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.repository.ClubMemberRepository;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description : 해당 Service Class 에서 주목해야 할점은
 *               - @Service 어노테이션 만으로도  Bean 에 주입이 가능하다는 점이다.
 *
 *               ❌ 해당 UserDetailService 를 상속받는 Service 를 생성 시
 *                  이전 SecurityConfig 의 InMemoryUserDetailsManager() 메서드를
 *                  제거해줘야한다! - 로그조차 안찍힘!
 *                  <b>
 *                  Error : No AuthenticationProvider found
 *                          for org.springframework.security.authentication.UsernamePasswordAuthenticationToken
 *                  </b>
 *                  이유는 ❔
 *                  ClubUSerDetailsService 가 Bean 으로 등록되면 이르 자동으로 Spring Security 에서
 *                  USerDetailsService 로 인식하기 때문임.
 * */
@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailService implements UserDetailsService {

    final private ClubMemberRepository clubMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService userName(ID):::" + username);

        Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);

        if(result.isEmpty()) throw new UsernameNotFoundException("Check Email or Social");

        ClubMember clubMember = result.get();

        log.info("-----------------------");
        log.info(clubMember);

        //User 을 상속받은 Class 이기떄문에 권한 체크가 가능함!
        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
          clubMember.getEmail(),
          clubMember.getPassword(),
          clubMember.isFromSocial(),
          clubMember.getRoleSet().stream()
                  .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                  .collect(Collectors.toList())
        );

        /**
         * Spring Security 에는 없는 것들 추가!
         * **/
        clubAuthMember.setName(clubMember.getName()); //진짜 이름
        clubAuthMember.setFromSocial(clubMember.isFromSocial()); //소셜 구분

        
        return clubAuthMember;
    }
}
