package com.webprogramming.middletermproject;

import com.webprogramming.middletermproject.repository.AirRepository;
import com.webprogramming.middletermproject.service.AirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private final AirRepository airRepository;

    @Autowired
    public AppConfig(AirRepository airRepository, AirRepository airRepository1) {
        this.airRepository = airRepository1;
    }
    @Bean
    public AirService airService() {
        return new AirService(airRepository);
    }
}
