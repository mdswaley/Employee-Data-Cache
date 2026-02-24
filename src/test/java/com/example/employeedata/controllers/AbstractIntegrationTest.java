package com.example.employeedata.controllers;

import com.example.employeedata.EmployeeTestContainer;
import com.example.employeedata.dto.EmployeeDTO;
import com.example.employeedata.entities.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

@Import(EmployeeTestContainer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
public class AbstractIntegrationTest {
    @Autowired
    WebTestClient webTestClient;

    EmployeeEntity employeeEntity = EmployeeEntity.builder()
            .name("MD Swaley")
            .age(22)
            .role("ADMIN")
            .email("md123@gmail.com")
            .dateOfJoining(LocalDateTime.now().toLocalDate())
            .salary(10000.00)
            .isActive(true)
            .build();

    EmployeeDTO employeeDTO = EmployeeDTO.builder()
            .id(1L)
            .name("MD Swaley")
            .age(22)
            .role("ADMIN")
            .email(employeeEntity.getEmail())
            .dateOfJoining(LocalDateTime.now().toLocalDate())
            .salary(10000.00)
            .isActive(true)
            .build();
}
