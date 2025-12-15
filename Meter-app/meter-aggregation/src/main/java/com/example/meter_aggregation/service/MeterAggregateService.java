package com.example.meter_aggregation.service;
import com.example.meter_aggregation.model.*;
import java.util.List;
import com.example.meter_aggregation.model.MeterAggregate;

public interface MeterAggregateService {
    void aggregateDaily();
    void aggregateWeekly();
    void aggregateMonthly();
    List<MeterAggregate> getAllAggregates();
	List<MeterReadingDto> fetchTodayReadings();
}