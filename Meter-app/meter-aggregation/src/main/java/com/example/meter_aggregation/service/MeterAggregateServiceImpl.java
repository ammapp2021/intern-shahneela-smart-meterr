/*package com.example.meter_aggregation.service;

import com.example.meter_aggregation.model.MeterAggregate;
import com.example.meter_aggregation.model.MeterReadingDto;
import com.example.meter_aggregation.repository.MeterAggregateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MeterAggregateServiceImpl implements MeterAggregateService {

    @Autowired
    private MeterAggregateRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private final double UNIT_RATE = 35;
    private final String STEP1_SERVICE_URL = "http://smart-meter:8080/meter/readings";

    private LocalDate parseReadingDate(String timestamp) {
        try {
            String datePart = timestamp.split("T")[0];
            return LocalDate.parse(datePart, DateTimeFormatter.ofPattern("yyyy-M-d"));
        } catch (Exception e) {
            System.out.println("Invalid timestamp skipped: " + timestamp);
            return null;
        }
    }

    private void saveAggregates(Map<String, Double> meterUnitsMap, String type, String date) {
        for (Map.Entry<String, Double> entry : meterUnitsMap.entrySet()) {
            String meterId = entry.getKey();
            double totalUnits = entry.getValue();

            Optional<MeterAggregate> existingOpt = repository.findByMeterIdAndTypeAndDate(meterId, type, date);

            MeterAggregate agg;
            if (existingOpt.isPresent()) {
                agg = existingOpt.get();
                agg.setTotalUnits(totalUnits); // replace with latest sum
            } else {
                agg = new MeterAggregate();
                agg.setMeterId(meterId);
                agg.setType(type);
                agg.setDate(date);
                agg.setTotalUnits(totalUnits);
            }

            agg.setTotalBill(agg.getTotalUnits() * UNIT_RATE);
            repository.save(agg);

            System.out.println("Saved aggregate: " + meterId + " | " + type + " | " + date + " | units: " + agg.getTotalUnits());
        }
    }

    private Map<String, Double> calculateAggregates(MeterReadingDto[] readings, LocalDate start, LocalDate end) {
        Map<String, Double> meterUnitsMap = new HashMap<>();
        for (MeterReadingDto r : readings) {
            LocalDate readingDate = parseReadingDate(r.getTimestamp());
            if (readingDate != null && !readingDate.isBefore(start) && !readingDate.isAfter(end)) {
                meterUnitsMap.put(r.getMeterId(),
                        meterUnitsMap.getOrDefault(r.getMeterId(), 0.0) + r.getUnits());
            }
        }
        return meterUnitsMap;
    }

    @Override
    public void aggregateDaily() {
        MeterReadingDto[] readings = restTemplate.getForObject(STEP1_SERVICE_URL, MeterReadingDto[].class);
        LocalDate today = LocalDate.now();

        Map<String, Double> meterUnitsMap = calculateAggregates(readings, today, today);
        saveAggregates(meterUnitsMap, "DAILY", today.toString());
    }

    @Override
    public void aggregateWeekly() {
        MeterReadingDto[] readings = restTemplate.getForObject(STEP1_SERVICE_URL, MeterReadingDto[].class);
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6);
        String dateRange = weekStart + " to " + today;

        Map<String, Double> meterUnitsMap = calculateAggregates(readings, weekStart, today);
        saveAggregates(meterUnitsMap, "WEEKLY", dateRange);
    }

    @Override
    public void aggregateMonthly() {
        MeterReadingDto[] readings = restTemplate.getForObject(STEP1_SERVICE_URL, MeterReadingDto[].class);
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        String dateRange = monthStart + " to " + today;

        Map<String, Double> meterUnitsMap = calculateAggregates(readings, monthStart, today);
        saveAggregates(meterUnitsMap, "MONTHLY", dateRange);
    }

    @Override
    public List<MeterAggregate> getAllAggregates() {
        return repository.findAll();
    }

    @Override
    public List<MeterReadingDto> fetchTodayReadings() {
        MeterReadingDto[] readings = restTemplate.getForObject(STEP1_SERVICE_URL, MeterReadingDto[].class);
        LocalDate today = LocalDate.now();
        List<MeterReadingDto> todayReadings = new ArrayList<>();

        for (MeterReadingDto r : readings) {
            LocalDate readingDate = parseReadingDate(r.getTimestamp());
            if (readingDate != null && readingDate.equals(today)) {
                todayReadings.add(r);
            }
        }
        return todayReadings;
    }
}*/
package com.example.meter_aggregation.service;

import com.example.meter_aggregation.model.MeterAggregate;
import com.example.meter_aggregation.model.MeterReadingDto;
import com.example.meter_aggregation.repository.MeterAggregateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MeterAggregateServiceImpl implements MeterAggregateService {

    @Autowired
    private MeterAggregateRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private final double UNIT_RATE = 35;

    private final String STEP1_SERVICE_URL = "http://smart-meter:8080/meter/readings";

    private final ZoneId KARACHI_ZONE = ZoneId.of("Asia/Karachi");
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

   
    private LocalDate parseReadingDate(String timestamp) {
        try {
            String datePart = timestamp.split("T")[0];
            return LocalDate.parse(datePart, DATE_FORMATTER);
        } catch (Exception e) {
            System.out.println("Invalid timestamp skipped: " + timestamp);
            return null;
        }
    }

  
    private void saveAggregates(Map<String, Double> meterUnitsMap, String type, String date) {
        meterUnitsMap.forEach((meterId, totalUnits) -> {
            List<MeterAggregate> existing = repository.findByTypeAndDate(type, date).stream()
                    .filter(a -> a.getMeterId().equals(meterId))
                    .toList();

            MeterAggregate agg;
            if (existing.isEmpty()) {
                agg = new MeterAggregate();
                agg.setMeterId(meterId);
                agg.setType(type);
                agg.setDate(date);
            } else {
                agg = existing.get(0);
            }
            double roundedUnits = Math.round(totalUnits * 100.0) / 100.0;
            double roundedBill = Math.round(roundedUnits * UNIT_RATE * 100.0) / 100.0;

            agg.setTotalUnits(roundedUnits);
            agg.setTotalBill(roundedBill);
            repository.save(agg);
        });
    }

    @Override
    public void aggregateDaily() {
        MeterReadingDto[] readings = restTemplate.getForObject(STEP1_SERVICE_URL, MeterReadingDto[].class);
        LocalDate today = LocalDate.now(KARACHI_ZONE); // fix timezone
        String dateStr = today.format(DATE_FORMATTER);

        Map<String, Double> meterUnitsMap = new HashMap<>();
        for (MeterReadingDto r : readings) {
            LocalDate readingDate = parseReadingDate(r.getTimestamp());
            if (readingDate != null && readingDate.equals(today)) {
                meterUnitsMap.put(r.getMeterId(),
                        meterUnitsMap.getOrDefault(r.getMeterId(), 0.0) + r.getUnits());
            }
        }

        saveAggregates(meterUnitsMap, "DAILY", dateStr);
    }

    @Override
    public void aggregateWeekly() {
        MeterReadingDto[] readings = restTemplate.getForObject(STEP1_SERVICE_URL, MeterReadingDto[].class);
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Karachi"));
        LocalDate weekStart = today.minusDays(6);
        String dateRange = weekStart + " to " + today;

        Map<String, Double> meterUnitsMap = new HashMap<>();
        for (MeterReadingDto r : readings) {
            LocalDate readingDate = parseReadingDate(r.getTimestamp());
            if (readingDate != null && !readingDate.isBefore(weekStart) && !readingDate.isAfter(today)) {
                // ✅ accumulate totalUnits per meterId
                meterUnitsMap.put(r.getMeterId(),
                        meterUnitsMap.getOrDefault(r.getMeterId(), 0.0) + r.getUnits());
            }
        }

        saveAggregates(meterUnitsMap, "WEEKLY", dateRange);
    }

    @Override
    public void aggregateMonthly() {
        MeterReadingDto[] readings = restTemplate.getForObject(STEP1_SERVICE_URL, MeterReadingDto[].class);
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Karachi"));
        LocalDate monthStart = today.withDayOfMonth(1);
        String dateRange = monthStart + " to " + today;

        Map<String, Double> meterUnitsMap = new HashMap<>();
        for (MeterReadingDto r : readings) {
            LocalDate readingDate = parseReadingDate(r.getTimestamp());
            if (readingDate != null && !readingDate.isBefore(monthStart) && !readingDate.isAfter(today)) {
                meterUnitsMap.put(r.getMeterId(),
                        meterUnitsMap.getOrDefault(r.getMeterId(), 0.0) + r.getUnits());
            }
        }

        saveAggregates(meterUnitsMap, "MONTHLY", dateRange);
    }
    @Override
    public List<MeterAggregate> getAllAggregates() {
        return repository.findAll();
    }

    @Override
    public List<MeterReadingDto> fetchTodayReadings() {
        MeterReadingDto[] readings = restTemplate.getForObject(STEP1_SERVICE_URL, MeterReadingDto[].class);
        LocalDate today = LocalDate.now(KARACHI_ZONE);
        List<MeterReadingDto> todayReadings = new ArrayList<>();

        for (MeterReadingDto r : readings) {
            LocalDate readingDate = parseReadingDate(r.getTimestamp());
            if (readingDate != null && readingDate.equals(today)) {
                todayReadings.add(r);
            }
        }
        return todayReadings;
    }
}

