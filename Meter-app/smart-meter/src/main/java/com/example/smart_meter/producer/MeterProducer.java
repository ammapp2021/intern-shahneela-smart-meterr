package com.example.smart_meter.producer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.smart_meter.model.MeterReading;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MeterProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String TOPIC = "meter-readings";
    private final ObjectMapper mapper = new ObjectMapper();

    public void sendReading(MeterReading reading) {
        try {
            Map<String, Object> payload = Map.of(
                "meterId", reading.getMeterId(),
                "units", reading.getUnits(),
                "timestamp", reading.getTimestamp()
            );
            kafkaTemplate.send(TOPIC, mapper.writeValueAsString(payload));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}