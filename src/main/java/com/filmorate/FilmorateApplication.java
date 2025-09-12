package com.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Filmorate application.<br>
 * This class bootstraps the Spring Boot application and starts the embedded server.
 */
@Slf4j
@SpringBootApplication
public class FilmorateApplication {
    public static void main(String[] args) {
        log.info("Starting FilmorateApplication...");
        SpringApplication.run(FilmorateApplication.class, args);
        log.info("FilmorateApplication started successfully!");
    }
}
