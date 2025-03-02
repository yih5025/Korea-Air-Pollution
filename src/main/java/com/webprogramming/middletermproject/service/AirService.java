package com.webprogramming.middletermproject.service;

import com.webprogramming.middletermproject.domain.Air;
import com.webprogramming.middletermproject.repository.AirRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AirService {
    private final AirRepository airRepository;

    public AirService(AirRepository airRepository) {
        this.airRepository = airRepository;
    }

    public void saveAirData(String parsedData) {
        List<Air> airList = new ArrayList<>();

        String[] lines = parsedData.split("\n");
        for (String line : lines) {
            Air air = convertToAir(line);
            airList.add(air);
        }

        airRepository.saveAll(airList);
    }

    private Air convertToAir(String line) {
        try {
            String[] fields = line.split(",");
            String dataTime = fields[0].split("=")[1];
            String stationName = fields[1].split("=")[1];
            Double so2Value = Double.parseDouble(fields[2].split("=")[1]);
            Double coValue = Double.parseDouble(fields[3].split("=")[1]);
            Double o3Value = Double.parseDouble(fields[4].split("=")[1]);
            Double no2Value = Double.parseDouble(fields[5].split("=")[1]);
            Double pm10Value = Double.parseDouble(fields[6].split("=")[1]); // 변경된 부분
            Double pm25Value = Double.parseDouble(fields[7].split("=")[1]); // 변경된 부분

            Air air = new Air();
            air.setDataTime(dataTime);
            air.setStationName(stationName);
            air.setSo2Value(so2Value);
            air.setCoValue(coValue);
            air.setO3Value(o3Value);
            air.setNo2Value(no2Value);
            air.setPm10Value(pm10Value.intValue()); // Double을 Integer로 변환하여 설정
            air.setPm25Value(pm25Value.intValue()); // Double을 Integer로 변환하여 설정

            return air;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Air> findAirList() {
        return airRepository.findAll();
    }

    public List<Air> findByStationName(String stationName) {
        System.out.println(stationName);
        return airRepository.findByStationName(stationName);
    }


}
