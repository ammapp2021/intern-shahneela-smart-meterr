package com.example.meter_aggregation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.meter_aggregation.model.MeterAggregate;
import com.example.meter_aggregation.service.MeterAggregateService;

@RestController
@RequestMapping("/aggregates")
public class MeterAggregateController {

    @Autowired
    private MeterAggregateService service;

    @GetMapping
    public List<MeterAggregate> getAll() {
        return service.getAllAggregates();
    }

    @GetMapping("/daily")
    public List<MeterAggregate> getDaily(@RequestParam String date) {
        return service.getAllAggregates().stream()
                .filter(a -> a.getType().equals("DAILY") && a.getDate().equals(date))
                .toList();
    }

    @GetMapping("/weekly")
    public List<MeterAggregate> getWeekly(@RequestParam String startDate, @RequestParam String endDate) {
        return service.getAllAggregates().stream()
                .filter(a -> a.getType().equals("WEEKLY") && a.getDate().equals(startDate + " to " + endDate))
                .toList();
    }

    @GetMapping("/monthly")
    public List<MeterAggregate> getMonthly(@RequestParam String month) {
        return service.getAllAggregates().stream()
                .filter(a -> a.getType().equals("MONTHLY") && a.getDate().startsWith(month))
                .toList();
    }
}