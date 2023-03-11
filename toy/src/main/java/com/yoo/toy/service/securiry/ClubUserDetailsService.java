package com.yoo.toy.service.securiry;

import com.yoo.toy.dto.security.ClubAuthMemberDTO;
import com.yoo.toy.entity.ClubMember;
import com.yoo.toy.repository.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {

    private final ClubMemberRepository clubMemberRepository;

    /**
     * @Description : 일반 적인 로그인 방법으로 로그인 되었을 시 접근 되는 Service
     *
     * @param  : String username (ID - 현 프로젝트에서는 Email)
     *
     * @return : UserDetails clubAuthMember
     *          👉 반환 타입이 UserDetails이지만 ClubAuthMember를 반환할 수 있는 이유는
     *             User를 상속 받아서 사용하였기 떄문이다.
     * **/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService loadUSerByUserName ::: {}", username);

        Optional<ClubMember> result = clubMemberRepository.findByEmail(username,false);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("Check User Name");
        }//if

        ClubMember clubMember = result.get();

        log.info("clubMember Info ::: {}",clubMember );


        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(),
                clubMember.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                        .collect(Collectors.toList())
        );

        clubAuthMember.setName(clubMember.getName());
        clubAuthMember.setFromSocial(clubMember.isFromSocial());

        return clubAuthMember;
    }
}
