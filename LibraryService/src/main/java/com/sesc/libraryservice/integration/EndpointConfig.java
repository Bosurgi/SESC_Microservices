package com.sesc.libraryservice.integration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration file to set up the endpoints for communication with the finance service
 */
@Configuration
@ConfigurationProperties(prefix = "endpoint")
@Getter
@Setter
public class EndpointConfig {
    private String financeHost;
    private String financeSendInvoice;
}
