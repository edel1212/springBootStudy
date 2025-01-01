package com.yoo.multipleDB.repository.primary;

import com.yoo.multipleDB.entity.primary.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
}
