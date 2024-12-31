package com.yoo.multipleDB.repository.sub;

import com.yoo.multipleDB.entity.sub.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatRepository extends JpaRepository<Cat, Long> {
}
