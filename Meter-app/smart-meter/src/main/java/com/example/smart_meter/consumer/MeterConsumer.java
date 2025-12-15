package com.example.smart_meter.consumer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.smart_meter.model.MeterReading;
import com.example.smart_meter.repository.MeterReadingRepository;
import com.example.smart_meter.service.MeterService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MeterConsumer {

    @Autowired
    private MeterService service;

    private ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "meter-readings", groupId = "meter-group")
    public void consume(String message) throws Exception {
        Map<String, Object> map = mapper.readValue(message, Map.class);

        MeterReading reading = new MeterReading();
        reading.setMeterId((String) map.get("meterId"));
        reading.setUnits(Double.parseDouble(map.get("units").toString()));
        reading.setTimestamp((String) map.get("timestamp"));

        service.saveReading(reading);

        System.out.println("Saved: " + reading.getMeterId() + " -> " + reading.getUnits() + " @ " + reading.getTimestamp());
    }
}