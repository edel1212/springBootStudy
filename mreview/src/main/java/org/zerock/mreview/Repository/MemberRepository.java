package org.zerock.mreview.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.mreview.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
