package com.yoo.multipleDB_mongo.repository;

import com.yoo.multipleDB_mongo.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
}
