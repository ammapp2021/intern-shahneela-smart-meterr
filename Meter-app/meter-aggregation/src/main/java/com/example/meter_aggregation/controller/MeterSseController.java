/*package com.example.meter_aggregation.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.meter_aggregation.model.MeterAggregate;
import com.example.meter_aggregation.service.MeterAggregateService;

@RestController
@RequestMapping("/stream")
@CrossOrigin(origins = "http://localhost:5173")
public class MeterSseController {

    @Autowired
    private MeterAggregateService service;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping("/meter-updates")
    public SseEmitter streamMeterUpdates() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // infinite timeout

        new Thread(() -> {
            try {
                while (true) {
                    List<MeterAggregate> allAggregates = service.getAllAggregates();

                    LocalDate today = LocalDate.now();
                    LocalDate weekStart = today.minusDays(6); // last 7 days
                    LocalDate monthStart = today.withDayOfMonth(1); // start of month

                    // ===== Summary =====
                    double dailyTotalUnits = allAggregates.stream()
                            .filter(a -> "DAILY".equals(a.getType()) && a.getDate().equals(formatter.format(today)))
                            .mapToDouble(MeterAggregate::getTotalUnits)
                            .sum();
                    double dailyTotalBill = allAggregates.stream()
                            .filter(a -> "DAILY".equals(a.getType()) && a.getDate().equals(formatter.format(today)))
                            .mapToDouble(MeterAggregate::getTotalBill)
                            .sum();

                    double weeklyTotalUnits = allAggregates.stream()
                            .filter(a -> "WEEKLY".equals(a.getType()))
                            .filter(a -> {
                                String[] range = a.getDate().split(" to ");
                                LocalDate start = LocalDate.parse(range[0], formatter);
                                LocalDate end = LocalDate.parse(range[1], formatter);
                                return !start.isAfter(weekStart) && !end.isBefore(today);
                            })
                            .mapToDouble(MeterAggregate::getTotalUnits)
                            .sum();
                    double weeklyTotalBill = allAggregates.stream()
                            .filter(a -> "WEEKLY".equals(a.getType()))
                            .filter(a -> {
                                String[] range = a.getDate().split(" to ");
                                LocalDate start = LocalDate.parse(range[0], formatter);
                                LocalDate end = LocalDate.parse(range[1], formatter);
                                return !start.isAfter(weekStart) && !end.isBefore(today);
                            })
                            .mapToDouble(MeterAggregate::getTotalBill)
                            .sum();

                    double monthlyTotalUnits = allAggregates.stream()
                            .filter(a -> "MONTHLY".equals(a.getType()))
                            .filter(a -> {
                                String[] range = a.getDate().split(" to ");
                                LocalDate start = LocalDate.parse(range[0], formatter);
                                LocalDate end = LocalDate.parse(range[1], formatter);
                                return !start.isAfter(monthStart) && !end.isBefore(today);
                            })
                            .mapToDouble(MeterAggregate::getTotalUnits)
                            .sum();
                    double monthlyTotalBill = allAggregates.stream()
                            .filter(a -> "MONTHLY".equals(a.getType()))
                            .filter(a -> {
                                String[] range = a.getDate().split(" to ");
                                LocalDate start = LocalDate.parse(range[0], formatter);
                                LocalDate end = LocalDate.parse(range[1], formatter);
                                return !start.isAfter(monthStart) && !end.isBefore(today);
                            })
                            .mapToDouble(MeterAggregate::getTotalBill)
                            .sum();

                    Map<String, Object> summary = new HashMap<>();
                    summary.put("daily", Map.of(
                            "date", formatter.format(today),
                            "totalUnits", dailyTotalUnits,
                            "totalBill", dailyTotalBill
                    ));
                    summary.put("weekly", Map.of(
                            "dateRange", formatter.format(weekStart) + " to " + formatter.format(today),
                            "totalUnits", weeklyTotalUnits,
                            "totalBill", weeklyTotalBill
                    ));
                    summary.put("monthly", Map.of(
                            "dateRange", formatter.format(monthStart) + " to " + formatter.format(today),
                            "totalUnits", monthlyTotalUnits,
                            "totalBill", monthlyTotalBill
                    ));

                    // ===== Daily meters =====
                    List<MeterAggregate> dailyMeters = allAggregates.stream()
                            .filter(a -> "DAILY".equals(a.getType()) && a.getDate().equals(formatter.format(today)))
                            .toList();

                    Map<String, Object> response = new HashMap<>();
                    response.put("summary", summary);
                    response.put("meters", Map.of("daily", dailyMeters));

                    emitter.send(response, MediaType.APPLICATION_JSON);

                    Thread.sleep(60_000); // 1 minute
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }
}*/
package com.example.meter_aggregation.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.meter_aggregation.model.MeterAggregate;
import com.example.meter_aggregation.service.MeterAggregateService;

@RestController
@RequestMapping("/stream")
@CrossOrigin(origins = "http://localhost:5173")
public class MeterSseController {

    @Autowired
    private MeterAggregateService service;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final ZoneId KARACHI_ZONE = ZoneId.of("Asia/Karachi");

    @GetMapping("/meter-updates")
    public SseEmitter streamMeterUpdates() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // infinite timeout

        new Thread(() -> {
            try {
                while (true) {
                    List<MeterAggregate> allAggregates = service.getAllAggregates();

                    LocalDate today = LocalDate.now(KARACHI_ZONE);
                    LocalDate weekStart = today.minusDays(6); 
                    LocalDate monthStart = today.withDayOfMonth(1); 

                    
                    double dailyTotalUnits = allAggregates.stream()
                            .filter(a -> "DAILY".equals(a.getType()) && a.getDate().equals(DATE_FORMATTER.format(today)))
                            .mapToDouble(MeterAggregate::getTotalUnits)
                            .sum();
                    double dailyTotalBill = allAggregates.stream()
                            .filter(a -> "DAILY".equals(a.getType()) && a.getDate().equals(DATE_FORMATTER.format(today)))
                            .mapToDouble(MeterAggregate::getTotalBill)
                            .sum();

                    double weeklyTotalUnits = allAggregates.stream()
                            .filter(a -> "WEEKLY".equals(a.getType()))
                            .filter(a -> {
                                String[] range = a.getDate().split(" to ");
                                LocalDate start = LocalDate.parse(range[0], DATE_FORMATTER);
                                LocalDate end = LocalDate.parse(range[1], DATE_FORMATTER);
                                return !start.isAfter(weekStart) && !end.isBefore(today);
                            })
                            .mapToDouble(MeterAggregate::getTotalUnits)
                            .sum();
                    double weeklyTotalBill = allAggregates.stream()
                            .filter(a -> "WEEKLY".equals(a.getType()))
                            .filter(a -> {
                                String[] range = a.getDate().split(" to ");
                                LocalDate start = LocalDate.parse(range[0], DATE_FORMATTER);
                                LocalDate end = LocalDate.parse(range[1], DATE_FORMATTER);
                                return !start.isAfter(weekStart) && !end.isBefore(today);
                            })
                            .mapToDouble(MeterAggregate::getTotalBill)
                            .sum();

                    double monthlyTotalUnits = allAggregates.stream()
                            .filter(a -> "MONTHLY".equals(a.getType()))
                            .filter(a -> {
                                String[] range = a.getDate().split(" to ");
                                LocalDate start = LocalDate.parse(range[0], DATE_FORMATTER);
                                LocalDate end = LocalDate.parse(range[1], DATE_FORMATTER);
                                return !start.isAfter(monthStart) && !end.isBefore(today);
                            })
                            .mapToDouble(MeterAggregate::getTotalUnits)
                            .sum();
                    double monthlyTotalBill = allAggregates.stream()
                            .filter(a -> "MONTHLY".equals(a.getType()))
                            .filter(a -> {
                                String[] range = a.getDate().split(" to ");
                                LocalDate start = LocalDate.parse(range[0], DATE_FORMATTER);
                                LocalDate end = LocalDate.parse(range[1], DATE_FORMATTER);
                                return !start.isAfter(monthStart) && !end.isBefore(today);
                            })
                            .mapToDouble(MeterAggregate::getTotalBill)
                            .sum();

                  
                    dailyTotalUnits = Math.round(dailyTotalUnits * 100.0) / 100.0;
                    dailyTotalBill = Math.round(dailyTotalBill * 100.0) / 100.0;
                    weeklyTotalUnits = Math.round(weeklyTotalUnits * 100.0) / 100.0;
                    weeklyTotalBill = Math.round(weeklyTotalBill * 100.0) / 100.0;
                    monthlyTotalUnits = Math.round(monthlyTotalUnits * 100.0) / 100.0;
                    monthlyTotalBill = Math.round(monthlyTotalBill * 100.0) / 100.0;

                    Map<String, Object> summary = new HashMap<>();
                    summary.put("daily", Map.of(
                            "date", DATE_FORMATTER.format(today),
                            "totalUnits", dailyTotalUnits,
                            "totalBill", dailyTotalBill
                    ));
                    summary.put("weekly", Map.of(
                            "dateRange", DATE_FORMATTER.format(weekStart) + " to " + DATE_FORMATTER.format(today),
                            "totalUnits", weeklyTotalUnits,
                            "totalBill", weeklyTotalBill
                    ));
                    summary.put("monthly", Map.of(
                            "dateRange", DATE_FORMATTER.format(monthStart) + " to " + DATE_FORMATTER.format(today),
                            "totalUnits", monthlyTotalUnits,
                            "totalBill", monthlyTotalBill
                    ));

               
                    List<MeterAggregate> dailyMeters = allAggregates.stream()
                            .filter(a -> "DAILY".equals(a.getType()) && a.getDate().equals(DATE_FORMATTER.format(today)))
                            .toList();

             
                    dailyMeters.forEach(m -> {
                        m.setTotalUnits(Math.round(m.getTotalUnits() * 100.0) / 100.0);
                        m.setTotalBill(Math.round(m.getTotalBill() * 100.0) / 100.0);
                    });

                    Map<String, Object> response = new HashMap<>();
                    response.put("summary", summary);
                    response.put("meters", Map.of("daily", dailyMeters));

                    emitter.send(response, MediaType.APPLICATION_JSON);

                    Thread.sleep(60_000); // 1 minute
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }
}

