package com.yoo.ex04.repository;

import com.yoo.ex04.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
