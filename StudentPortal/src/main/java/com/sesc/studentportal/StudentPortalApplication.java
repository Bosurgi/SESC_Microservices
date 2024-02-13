package com.sesc.studentportal;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(value = "my-hilla-app")
public class StudentPortalApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(StudentPortalApplication.class, args);
    }

}
