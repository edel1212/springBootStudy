package com.yoo.multipleDB.repository.sub;

import com.yoo.multipleDB.entity.sub.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
