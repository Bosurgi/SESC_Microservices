package com.sesc.studentportal.misc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * This class is used to configure the WebClient Builder used to make API calls to different Services.
 */
@Configuration
public class WebClientConfig {

    /**
     * This Bean is used to instantiate the Builder in other Services.
     *
     * @return the WebClient Builder
     */
    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }
}
