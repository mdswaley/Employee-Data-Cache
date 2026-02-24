package com.example.employeedata.controllers;

import com.example.employeedata.advices.ApiResponse;
import com.example.employeedata.dto.EmployeeDTO;
import com.example.employeedata.entities.EmployeeEntity;
import com.example.employeedata.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;


import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class EmployeeControllerTest extends AbstractIntegrationTest{

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
    }

    @Test
    void GetEmpById_whenEmpIsPresent(){
        EmployeeEntity savedEmp = employeeRepository.save(employeeEntity);

        webTestClient.get()
                .uri("/employees/{employeeId}",savedEmp.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<ApiResponse<EmployeeDTO>>() {})
                .value(response -> {
                    EmployeeDTO employeeDTO1 = response.getData();
                    assertThat(employeeDTO1.getSalary()).isEqualTo(savedEmp.getSalary());
                    assertThat(employeeDTO1.getAge()).isEqualTo(savedEmp.getAge());
                });
        log.info("Get employee By id is called.");
    }

    @Test
    void GetEmpById_WhenEmpIsNotPresent(){

    }




}