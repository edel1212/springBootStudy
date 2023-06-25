package com.yoo.statistics.repository.dsl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.yoo.statistics.entity.Electricity;
import com.yoo.statistics.entity.QElectricity;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        Double result = jpqlQuery.select(qElectricity.value.sum())
                .where(qElectricity.regDate.between(today.atStartOfDay(), today.plusDays(1).atStartOfDay()))
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
