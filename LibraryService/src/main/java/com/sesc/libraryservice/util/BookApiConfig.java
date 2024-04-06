package com.sesc.libraryservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration file to fetch the API key for Google Book API
 */
@Configuration
public class BookApiConfig {
    @Value("${library.key}")
    private String apiKey;

    @Bean
    public String apiKey() {
        return apiKey;
    }
}
