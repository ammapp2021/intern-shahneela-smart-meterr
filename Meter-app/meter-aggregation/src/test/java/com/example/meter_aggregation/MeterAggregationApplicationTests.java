package com.example.meter_aggregation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

import com.example.meter_aggregation.model.MeterAggregate;
import com.example.meter_aggregation.model.MeterReadingDto;
import com.example.meter_aggregation.repository.MeterAggregateRepository;
import com.example.meter_aggregation.service.MeterAggregateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(properties = "spring.profiles.active=test")
class MeterAggregationApplicationTests {

    
    @Test
    void contextLoads() {
        //assertThat(aggregateService).isNotNull();
    }

    
}
