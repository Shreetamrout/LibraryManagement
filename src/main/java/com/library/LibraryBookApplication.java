package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.library.repository")
public class LibraryBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryBookApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("Library Management System Started!");
        System.out.println("API Documentation: http://localhost:8080/swagger-ui.html");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("========================================\n");
    }
}
