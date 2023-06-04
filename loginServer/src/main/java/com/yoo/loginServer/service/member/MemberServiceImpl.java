package com.yoo.loginServer.service.member;

import com.yoo.loginServer.dto.member.MemberDTO;
import com.yoo.loginServer.entity.member.Member;
import com.yoo.loginServer.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public boolean findMember(MemberDTO memberDTO) {

        log.info("memberDTO ::: {}", memberDTO);

        Member member = memberRepository.findById(memberDTO.getId()).orElse(null);
        log.info("member :::: {}",member);
        return member == null ? false : true;
    }

    @Override
    public String registerMember(MemberDTO memberDTO) {
        Member member = memberRepository.save(this.dtoToEntity(memberDTO));
        return member.getId();
    }
}
