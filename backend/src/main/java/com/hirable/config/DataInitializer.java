package com.hirable.config;

import com.hirable.util.SampleResumeGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initializeSampleData(SampleResumeGenerator resumeGenerator) {
        return args -> {
            try {
                logger.info("Initializing sample resume files...");
                resumeGenerator.generateSampleResumes();
                logger.info("Sample resume files created successfully");
            } catch (Exception e) {
                logger.error("Error initializing sample data", e);
            }
        };
    }
}
