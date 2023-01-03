package com.yoo.toy.repository;

import com.yoo.toy.entity.Dog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
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


    //Sort With Service Pageable
    Page<Dog> findByDnumBetween(Long from, Long to, Pageable pageable);

    //Delete
    void deleteByDnumLessThan(long dnum);

    ////////////////////////////////////////////
    // JPQL
    ////////////////////////////////////////////

    @Query("SELECT d FROM Dog d ORDER BY d.dnum DESC")
    List<Dog> getListDesc();

    @Query("UPDATE Dog d SET d.age = :age WHERE d.dnum = :dnum")
    @Modifying
    @Transactional
    /**
     *  Error - org.hibernate.hql.internal.QueryExecutionRequestException
     *          : Not supported for DML operations <<  @Modifyin 없을 경우
     *
     *        - nested exception is javax.persistence.TransactionRequiredException
     *          : Executing an update/delete query <<  @Transactional 없을 경우
     * */
    int updateAge(int age, Long dnum);
    
    /**
     * 변수명이 Query 변수와 맞으면 @Param("??")은 필요가 없음
     * 다만 #{#..} 은 사용해 줘야함
     * */
    @Query("UPDATE Dog d SET d.age = :#{dog.age} WHERE d.dnum = :#{#dog.dnum}")
    @Modifying
    @Transactional
    int updateAgeObjectParamVer(Dog dog);


    // JPQL With Pageable

    /**
     * count Query 미적용
     * Hibernate: 
     *     select
     *         dog0_.dnum as dnum1_0_,
     *         dog0_.age as age2_0_,
     *         dog0_.name as name3_0_ 
     *     from
     *         tbl_dog dog0_ 
     *     where
     *         dog0_.dnum>? 
     *     order by
     *         dog0_.dnum desc limit ?
     * Hibernate: 
     *     select
     *         count(dog0_.dnum) as col_0_0_ 
     *     from
     *         tbl_dog dog0_ 
     *     where
     *         dog0_.dnum>?
     * 
     * 
     * ---------------------------
     * count Query 적용
     *
     * Hibernate:
     *     select
     *         dog0_.dnum as dnum1_0_,
     *         dog0_.age as age2_0_,
     *         dog0_.name as name3_0_
     *     from
     *         tbl_dog dog0_
     *     where
     *         dog0_.dnum>?
     *     order by
     *         dog0_.dnum desc limit ?
     * Hibernate:
     *     select
     *         count(dog0_.dnum) as col_0_0_
     *     from
     *         tbl_dog dog0_
     *     where
     *         dog0_.dnum>?
     *
     * */
    @Query(value = "SELECT d FROM Dog d WHERE  d.dnum > :dnum"
    , countQuery = "SELECT COUNT(d) FROM Dog d WHERE d.dnum > :dnum"
    ) //없어도 똑같은데?
    Page<Dog> getListWithJPQL(Long dnum, Pageable pageable);
    
    
    //JPQL -- return Object[] 사용
    //CURRENT_DATE 같은 경우 Dog Class에 없던 값임!
    @Query(value = "SELECT d.dnum, d.age, d.name, CURRENT_DATE FROM Dog d WHERE  d.dnum > :dnum")
    Page<Object> getListWithJQPLAndObject(Long dnum, Pageable pageable);
    
}
