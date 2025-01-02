package com.yoo.p6spy.repository;

import com.yoo.p6spy.entity.Member;
import com.yoo.p6spy.repository.dsl.MemberSupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String>, MemberSupport {
}
