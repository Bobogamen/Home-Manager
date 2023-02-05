package com.home_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomeManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeManagerApplication.class, args);
        System.out.println("Домоуправител е стартиран...");
    }
}
