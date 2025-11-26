package com.hirable;

import com.hirable.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HirableApplication {
    public static void main(String[] args) {
        SpringApplication.run(HirableApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeData(AuthenticationService authenticationService) {
        return args -> {
            authenticationService.initializeDemoUsers();
        };
    }
}
