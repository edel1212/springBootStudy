package com.yoo.exGraphQL.repository;

import com.yoo.exGraphQL.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
