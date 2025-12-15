package com.example.smart_meter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.example.smart_meter.model.MeterReading;
import com.example.smart_meter.service.MeterService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest   // <-- REQUIRED
class SmartMeterApplicationTests {

    
    @Test
    void contextLoads() {
        //assertThat(meterService).isNotNull();
    }

    

    
}
