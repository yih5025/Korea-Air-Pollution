package com.webprogramming.middletermproject.repository;

import com.webprogramming.middletermproject.domain.Air;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AirRepository extends JpaRepository<Air, Long>, Repository {
    List<Air> findByStationName(String stationName);
}
