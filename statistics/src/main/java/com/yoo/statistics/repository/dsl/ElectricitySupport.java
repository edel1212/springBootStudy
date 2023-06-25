package com.yoo.statistics.repository.dsl;

public interface ElectricitySupport {
    void getElectricityOfToday();

    void getElectricityOfMonth();

    void getElectricityOfYear();

    void getElectricityByHourOfDay();
}
