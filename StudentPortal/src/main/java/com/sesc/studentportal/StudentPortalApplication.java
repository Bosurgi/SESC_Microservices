package com.sesc.studentportal;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentPortalApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(StudentPortalApplication.class, args);
    }

}
