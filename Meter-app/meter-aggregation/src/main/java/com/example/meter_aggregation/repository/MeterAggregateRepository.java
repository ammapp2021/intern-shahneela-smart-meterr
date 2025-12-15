package com.example.meter_aggregation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.meter_aggregation.model.MeterAggregate;

@Repository
public interface MeterAggregateRepository extends JpaRepository<MeterAggregate, Long> {

    @Query(value = "SELECT * FROM meter_aggregates WHERE type = :type AND date = :date", nativeQuery = true)
    List<MeterAggregate> findByTypeAndDate(@Param("type") String type, @Param("date") String date);

    @Query(value = "SELECT * FROM meter_aggregates WHERE type = :type AND date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<MeterAggregate> findByTypeAndDateBetween(@Param("type") String type,
                                                  @Param("startDate") String startDate,
                                                  @Param("endDate") String endDate);
    Optional<MeterAggregate> findByMeterIdAndTypeAndDate(String meterId, String type, String date);}


    /*@Query(value = "SELECT meter_id, SUM(total_units) AS totalUnits, SUM(total_bill) AS totalBill " +
                   "FROM meter_aggregates WHERE type = :type AND date = :date GROUP BY meter_id", nativeQuery = true)
    List<Object[]> aggregateByTypeAndDate(@Param("type") String type, @Param("date") String date);

    @Query(value = "SELECT meter_id, SUM(total_units) AS totalUnits, SUM(total_bill) AS totalBill " +
                   "FROM meter_aggregates WHERE type = :type AND date BETWEEN :start AND :end GROUP BY meter_id", nativeQuery = true)
    List<Object[]> aggregateByTypeAndDateRange(@Param("type") String type,
                                               @Param("start") String start,
                                               @Param("end") String end);}*/