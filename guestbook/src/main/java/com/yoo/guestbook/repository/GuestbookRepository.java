package com.yoo.guestbook.repository;

import com.yoo.guestbook.entitiy.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestbookRepository extends JpaRepository<Guestbook,Long> {
}
