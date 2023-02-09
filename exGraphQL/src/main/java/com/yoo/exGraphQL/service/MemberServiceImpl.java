package com.yoo.exGraphQL.service;

import com.yoo.exGraphQL.dto.MemberDTO;
import com.yoo.exGraphQL.repository.MemberRepository;
import com.yoo.exGraphQL.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;


    @Override
    public MemberDTO findMember(String email) {
        log.info("findMember ::: {}", email);
        return this.entityToDto(memberRepository.findById(email).get());
    }

    @Override
    public List<MemberDTO> allFindMembers() {
        log.info("findAllMember ");
        return memberRepository.findAll().stream()
                .map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public String registerMember(MemberDTO member) {
        log.info("registerMember :: {}", member);
        Member result = memberRepository.save(this.dtoToEntity(member));
        return result.getEmail();
    }

    @Override
    public Boolean updateMember(MemberDTO member) {
        log.info("updateMember :: {}", member);
        if (memberRepository.findById(member.getEmail()).get() == null){
            return  false;
        }
        memberRepository.save(this.dtoToEntity(member));
        return true;
    }

    @Override
    public Boolean deleteMember(String email) {
        log.info("deleteMember :: {}", email);
        memberRepository.deleteById(email);
        return memberRepository.findById(email).get() != null;
    }
}
