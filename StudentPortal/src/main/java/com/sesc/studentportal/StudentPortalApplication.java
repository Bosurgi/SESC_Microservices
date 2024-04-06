package com.sesc.studentportal;

import com.sesc.studentportal.misc.EndpointConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(EndpointConfig.class)
public class StudentPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentPortalApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(UserRepository users, StudentRepository students) {
//        return args -> {
//            users.save(new User("admin", "admin", "ROLE_ADMIN"));
//            users.save(new User("user", "user", "ROLE_USER, ROLE_STUDENT"));
//        };
//    }

}
