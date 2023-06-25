package com.yoo.statistics.repository;

import com.yoo.statistics.entity.Electricity;
import com.yoo.statistics.repository.dsl.ElectricitySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectricityRepository extends JpaRepository<Electricity,Long>, ElectricitySupport {
    // 오늘 등록 쿼리
    // SELECT * FROM electricity WHERE DATE(reg_date) = CURDATE();

    // 이번 달
    // SELECT * FROM electricity WHERE YEAR(reg_date) = YEAR(CURDATE()) AND MONTH(reg_date) = MONTH(CURDATE()) order by reg_date  DESC ;

    // 지난 달
    // SELECT * FROM electricity WHERE YEAR(reg_date) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND MONTH(reg_date) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH));

    // 1년 전
    //SELECT * FROM electricity WHERE YEAR(reg_date) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 YEAR));

    /**
     * And 조건을 붙여가며 날짜를 조정 가능하다
     * 이전 경우 (DATE_SUB) 사용 :: YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND MONTH(reg_date) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH));
     *
     * 이후 경우 (DATE_ADD) 사용 :: YEAR(DATE_ADD(CURDATE(), INTERVAL 1 MONTH)) AND MONTH(regDate) = MONTH(DATE_ADD(CURDATE(), INTERVAL 1 MONTH));
     * */

}
