package com.yoo.toy.repository;

import com.yoo.toy.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DogRepository extends JpaRepository<Dog, Long> {

    //Select Query

    Dog findBydnum(Long dnum);

    List<Dog> findByAge(int age);

    List<Dog> findByName(String name);

    //First, Top
    List<Dog> findFirst2ByAge(int age);

    List<Dog> findLast1ByAge(int age);

    @Deprecated
    List<Dog> findLast2ByAge(int age); // Last 는 기능이 없음


    // After, Before, GreaterThan, LessThan
    List<Dog> findByAgeGreaterThan(int age); // 초과

    List<Dog> findByAgeGreaterThanEqual(int age); //이상

    List<Dog> findByAgeLessThan(int age); // 미만

    List<Dog> findByAgeLessThanEqual(int age); //이하


    // is(Not)Null
    List<Dog> findByAgeIsNotNull();
    List<Dog> findByAgeIsNull();

    // In
    List<Dog> findByNameIn(List<String> names);

    //Like
    List<Dog> findByNameStartingWith(String name);
    List<Dog> findByNameEndingWith(String name);
    List<Dog> findByNameLike(String name);
    List<Dog> findByNameContains(String name);

    //Sort
    List<Dog> findByNameOrderByDnumDesc(String name);


}
