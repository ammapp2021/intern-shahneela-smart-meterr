package com.example.smart_meter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.smart_meter.model.MeterReading;
import com.example.smart_meter.repository.MeterReadingRepository;

@Service
public class MeterServiceImpl implements MeterService {

    @Autowired
    private MeterReadingRepository repository;

    @Override
    public MeterReading saveReading(MeterReading reading) {
       
        repository.insertReading(reading.getMeterId(), reading.getUnits(), reading.getTimestamp());
        return reading;
    }

    @Override
    public List<MeterReading> getReadingsByMeter(String meterId) {
        return repository.findByMeter(meterId);
    }
    @Override
    public List<MeterReading> getAllReadings() {
        return repository.findAll();
    }
}