package com.example.smart_meter.service;

import java.util.List;

import com.example.smart_meter.model.MeterReading;

public interface MeterService {
    MeterReading saveReading(MeterReading reading);
    List<MeterReading> getReadingsByMeter(String meterId);
    List<MeterReading> getAllReadings();
}
