package com.home_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HomeManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeManagerApplication.class, args);
        System.out.println("Home manager is started...");
    }
}
