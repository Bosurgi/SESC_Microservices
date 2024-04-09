package com.sesc.libraryservice;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(DriverManagerDataSource.class)
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:testdb")
                .username("example")
                .password("")
                .build();
    }
}
