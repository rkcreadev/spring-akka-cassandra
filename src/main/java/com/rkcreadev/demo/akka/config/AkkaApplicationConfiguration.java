package com.rkcreadev.demo.akka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@EnableScheduling
public class AkkaApplicationConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean("tmpDir")
    public Path tmpDir() {
        try {
            return Files.createTempDirectory(null);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while got temporary directory");
        }
    }
}
