package com.weeklyPlanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.weeklyPlanner")
@ComponentScan(basePackages = {"com.weeklyPlanner.controller", "com.weeklyPlanner.model", "com.weeklyPlanner.repository", "com.weeklyPlanner.config", "com.weeklyPlanner.service"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}