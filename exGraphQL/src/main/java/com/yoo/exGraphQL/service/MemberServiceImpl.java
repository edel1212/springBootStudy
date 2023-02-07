package com.yoo.exGraphQL.service;

import com.yoo.exGraphQL.repository.MemberRepository;
import com.yoo.exGraphQL.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public List<Member> getList() {
        return memberRepository.findAll();
    }
}
