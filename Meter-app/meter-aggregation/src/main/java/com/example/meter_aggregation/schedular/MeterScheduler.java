package com.example.meter_aggregation.schedular;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.meter_aggregation.service.MeterAggregateService;

@Component
public class MeterScheduler {

    @Autowired
    private MeterAggregateService service;

  
    @Scheduled(cron = "0 */1 * * * *")
    public void runScheduler() {
        service.aggregateDaily();
        service.aggregateWeekly();
        service.aggregateMonthly();
        System.out.println("Scheduler ran: Aggregation completed.");
    }
}