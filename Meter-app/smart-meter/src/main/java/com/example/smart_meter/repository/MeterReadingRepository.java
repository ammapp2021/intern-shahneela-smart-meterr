package com.example.smart_meter.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.smart_meter.model.MeterReading;


import jakarta.transaction.Transactional;

@Repository
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {

	@Modifying
    @Transactional
    @Query(value = "INSERT INTO meter_readings (meter_id, units, timestamp) VALUES (:meterId, :units, :timestamp)", nativeQuery = true)
    void insertReading(@Param("meterId") String meterId,
                       @Param("units") double units,
                       @Param("timestamp") String timestamp);


    @Query(value = "SELECT * FROM meter_readings WHERE meter_id = :meterId", nativeQuery = true)
    List<MeterReading> findByMeter(@Param("meterId") String meterId);
    
}