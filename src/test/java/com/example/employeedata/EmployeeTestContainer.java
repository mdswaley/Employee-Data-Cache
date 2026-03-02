package com.example.employeedata;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class EmployeeTestContainer {
    @ServiceConnection
    MySQLContainer<?> mySQLContainer(){
        return new MySQLContainer<>("mysql:latest");
    }
}
