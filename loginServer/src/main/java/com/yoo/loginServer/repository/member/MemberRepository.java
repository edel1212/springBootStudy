package com.yoo.loginServer.repository.member;

import com.yoo.loginServer.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
