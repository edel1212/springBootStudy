package com.yoo.statistics.repository.dsl;

import com.querydsl.jpa.JPQLQuery;
import com.yoo.statistics.entity.Electricity;
import com.yoo.statistics.entity.QElectricity;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class ElectricitySupportImpl extends QuerydslRepositorySupport implements ElectricitySupport{

    public ElectricitySupportImpl() {
        super(Electricity.class);
    }


    @Override
    public void statistics() {

        log.info("!!!!");
        QElectricity qElectricity = QElectricity.electricity;

        JPQLQuery<Electricity> jpqlQuery = from(qElectricity);

        //jpqlQuery.where(qElectricity.regDate.after())

        List<Electricity> result = jpqlQuery.fetch();

        log.info("result::: {}",result);

    }
}
