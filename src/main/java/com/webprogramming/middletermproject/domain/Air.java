package com.webprogramming.middletermproject.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "AirData_table")
public class Air {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Double getSo2Value() {
        return so2Value;
    }

    public void setSo2Value(Double so2Value) {
        this.so2Value = so2Value;
    }

    public Double getCoValue() {
        return coValue;
    }

    public void setCoValue(Double coValue) {
        this.coValue = coValue;
    }

    public Double getO3Value() {
        return o3Value;
    }

    public void setO3Value(Double o3Value) {
        this.o3Value = o3Value;
    }

    public Double getNo2Value() {
        return no2Value;
    }

    public void setNo2Value(Double no2Value) {
        this.no2Value = no2Value;
    }

    public Integer getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(Integer pm10Value) {
        this.pm10Value = pm10Value;
    }

    public Integer getPm25Value() {
        return pm25Value;
    }

    public void setPm25Value(Integer pm25Value) {
        this.pm25Value = pm25Value;
    }

    @Column(name = "dataTime")
    private String dataTime;

    @Column(name = "stationName")
    private String stationName;

    @Column(name = "so2Value")
    private Double so2Value;

    @Column(name = "coValue")
    private Double coValue;

    @Column(name = "o3Value")
    private Double o3Value;

    @Column(name = "no2Value")
    private Double no2Value;

    @Column(name = "pm10Value")
    private Integer pm10Value;

    @Column(name = "pm25Value")
    private Integer pm25Value;

}
