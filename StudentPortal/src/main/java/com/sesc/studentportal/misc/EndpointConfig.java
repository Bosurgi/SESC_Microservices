package com.sesc.studentportal.misc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "endpoint")
@Getter
@Setter
public class EndpointConfig {
    private String libraryHost;
    private String libraryCreateStudent;
    private String financeHost;
    private String financeCreateStudent;
    private String financeUserStatus;
    private String financeModuleEnrol;

}
