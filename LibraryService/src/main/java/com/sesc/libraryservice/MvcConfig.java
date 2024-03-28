package com.sesc.libraryservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/changepassword").setViewName("changepassword");
        registry.addViewController("/api/v1/books").setViewName("books");
        registry.addViewController("/api/v1/transactions").setViewName("borrow");
        registry.addViewController("/api/v1/transactions").setViewName("account");
        registry.addViewController("/api/v1/transactions/return").setViewName("return");
        // Admin Views
        registry.addViewController("/api/v1/admin/students").setViewName("admin/students");
        registry.addViewController("/api/v1/admin/loans").setViewName("admin/loans");

    }
}
