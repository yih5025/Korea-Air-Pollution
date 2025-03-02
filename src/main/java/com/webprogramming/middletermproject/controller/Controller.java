package com.webprogramming.middletermproject.controller;

import com.webprogramming.middletermproject.domain.Air;
import com.webprogramming.middletermproject.service.AirService;
import com.webprogramming.middletermproject.service.ApiExplorer;
import com.webprogramming.middletermproject.service.InfluxDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    private AirService airService;

    @Autowired
    public Controller(AirService airService) {
        this.airService = airService;
    }

    @GetMapping("/")
    public String Home() {
        System.out.println("get:index");
        return "index";
    }
    @GetMapping("/SearchStationName")
    public String searchStationName() {
        return "SearchStationName";
    }

    @PostMapping("/SearchStationName")
    public String findStationName(Model model, @RequestParam String stationName) {
        System.out.println("PostMapping: " + stationName);
        List<Air> results = airService.findByStationName(stationName);
        model.addAttribute("airs", results); // 모든 결과를 모델에 추가
        return "airList";
    }

    @PostMapping("/StationName")
    public String getMeasurements(@RequestParam String stationName, RedirectAttributes redirectAttributes) {
        try {
            String[] values = stationName.split(",");
            String englishName = values[0];
            String koreanName = values[1];

            System.out.println(stationName);

            String parsedData = ApiExplorer.fetchDataAndParse(koreanName);

            System.out.println(parsedData);

            InfluxDB.saveDataToInfluxDB(parsedData);

            airService.saveAirData(parsedData);

            redirectAttributes.addAttribute("stationName", englishName);
            return "redirect:/StationNamePage";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/"; // 에러 발생 시 홈페이지로 리다이렉트
        }
    }

    @GetMapping("/StationNamePage")
    public String redirectToStationPage(@RequestParam String stationName, Model model) {
        model.addAttribute("stationName", stationName);
        model.addAttribute("firstPm10Value", InfluxDB.getFirstPm10Value());
        model.addAttribute("firstPm25Value", InfluxDB.getFirstPm25Value());
        InfluxDB.resetFirstPmValues();

        return stationName; // 전달된 stationName에 해당하는 html 페이지로 이동
    }

    @GetMapping("/AirList")
    public String airList(Model model) {
        List<Air> airs = airService.findAirList();
        model.addAttribute("airs", airs);
        return "airList";
    }

}
