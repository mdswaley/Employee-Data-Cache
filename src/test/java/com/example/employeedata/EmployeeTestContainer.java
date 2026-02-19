package com.example.employeedata;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration
public class EmployeeTestContainer {
    @Bean
    @ServiceConnection
    MySQLContainer<?> mySQLContainer(){
        return new MySQLContainer<>("mysql:latest");
    }
}
