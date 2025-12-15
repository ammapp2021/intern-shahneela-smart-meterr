package com.example.meter_aggregation.model;

public class MeterReadingDto {
    private String meterId;
    private String timestamp;
    private double units;
 
    public MeterReadingDto(String meterId, double units, String timestamp) {
        this.meterId = meterId;
        this.units = units;
        this.timestamp = timestamp;
    }
    public String getMeterId() { return meterId; }
    public void setMeterId(String meterId) { this.meterId = meterId; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public double getUnits() { return units; }
    public void setUnits(double units) { this.units = units; }
}