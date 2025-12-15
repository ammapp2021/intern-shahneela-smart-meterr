package com.example.smart_meter.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.smart_meter.model.MeterReading;
import com.example.smart_meter.producer.MeterProducer;
import com.example.smart_meter.service.MeterService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/meter")
public class MeterController {

    @Autowired
    private MeterProducer producer;

    @Autowired
    private MeterService service;

    @PostMapping("/send")
    public MeterReading sendReading(@RequestBody MeterReading reading) {

        producer.sendReading(reading);
        return reading;
    }

    @GetMapping("/{meterId}")
    public List<MeterReading> getReadings(@PathVariable String meterId) {
        return service.getReadingsByMeter(meterId);
    }
    @GetMapping("/readings")
    public List<MeterReading> getAllReadings() {
        return service.getAllReadings();
    }
}