package com.yoo.statistics.repository.dsl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.yoo.statistics.entity.Electricity;
import com.yoo.statistics.entity.QElectricity;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class ElectricitySupportImpl extends QuerydslRepositorySupport implements ElectricitySupport{

    public ElectricitySupportImpl() {
        super(Electricity.class);
    }


    @Override
    public void getElectricityOfToday() {

        QElectricity qElectricity = QElectricity.electricity;

        LocalDate today = LocalDate.now();

        JPQLQuery<Electricity> jpqlQuery = from(qElectricity);


        log.info("today.atStartOfDay()------------------");
        // 2023-06-25T00:00
        log.info(today.atStartOfDay());
        log.info("----------------------------------");


        log.info("today.plusDays(1).atStartOfDay()------------------");
        // 2023-06-25T23:59:59
        log.info(today.plusDays(1).atStartOfDay().minusSeconds(1));
        log.info("----------------------------------");

        Double result = jpqlQuery.select(qElectricity.value.sum())
                .where(qElectricity.regDate.between(today.atStartOfDay(), today.plusDays(1).atStartOfDay().minusSeconds(1)))
                .fetchOne();

        log.info("result::: {}",result);

    }

    @Override
    public void getElectricityOfMonth() {
        QElectricity qElectricity = QElectricity.electricity;

        YearMonth yearMonth = YearMonth.now();

        log.info("-----------------------------");
        // 2023-06
        log.info("yearMonth ::: {}",yearMonth);
        log.info("-----------------------------");

        // 2023-06-01T00:00:00.000+0900
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        // 2023-06-30T23:59:59.000+0900
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        JPQLQuery<Electricity> jpqlQuery = from(qElectricity);

        Double result = jpqlQuery.select(qElectricity.value.sum())
                .where(qElectricity.regDate.between(startDateTime, endDateTime))
                .fetchOne();

        log.info("result::: {}",result);
    }

    @Override
    public void getElectricityOfYear() {
        QElectricity qElectricity = QElectricity.electricity;

        Year year = Year.now();

        log.info("-----------------------------");
        // 2023
        log.info("year ::: {}",year);
        log.info("-----------------------------");

        // 2023-01-01T00:00:00.000+0900
        LocalDateTime startDateTime = year.atMonth(1).atDay(1).atStartOfDay();
        // 2023-12-31T23:59:59.000+0900
        LocalDateTime endDateTime = year.atMonth(12).atEndOfMonth().atTime(23, 59, 59);

        JPQLQuery<Electricity> jpqlQuery = from(qElectricity);

        Double result = jpqlQuery.select(qElectricity.value.sum())
                .where(qElectricity.regDate.between(startDateTime, endDateTime))
                .fetchOne();

        log.info("result::: {}",result);
    }

    @Override
    public void getElectricityByHourOfDay() {
        QElectricity qElectricity = QElectricity.electricity;
        LocalDate today = LocalDate.now();
        LocalDateTime startDateTime = today.atStartOfDay();
        LocalDateTime endDateTime = startDateTime.plusDays(1);

        JPQLQuery<Electricity> jpqlQuery = from(qElectricity);

        List<Tuple> result = jpqlQuery
                .select(qElectricity.regDate.hour(), qElectricity.value.sum())
                .from(qElectricity)
                .where(qElectricity.regDate.between(startDateTime, endDateTime))
                .groupBy(qElectricity.regDate.hour())
                .fetch();

        log.info("result ::: {}", result);

    }
}
