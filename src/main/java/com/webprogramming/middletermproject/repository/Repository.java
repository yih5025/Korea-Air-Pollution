package com.webprogramming.middletermproject.repository;

import com.webprogramming.middletermproject.domain.Air;

import java.util.List;

public interface Repository {
    List<Air> findByStationName(String stationName);
}
