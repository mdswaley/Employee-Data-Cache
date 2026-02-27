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


import java.time.LocalDateTime;

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
        log.info("Getting employee with given ID");
    }

    @Test
    void GetEmpById_WhenEmpIsNotPresent(){
        Long id = 1L;
        webTestClient.get()
                .uri("/employees/{employeeId}", id)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(new ParameterizedTypeReference<ApiResponse<EmployeeDTO>>() {})
                        .value(res ->{
                            assertThat(res.getData()).isNull();
                            assertThat(res.getError()).isNotNull();
                        });

        log.warn("User is not present with given ID");
    }

    @Test
    void CreateEmp_whenEmpIsNotPresentWithGivenEmail(){
        webTestClient.post()
                .uri("/employees")
                .bodyValue(employeeDTO)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(new ParameterizedTypeReference<ApiResponse<EmployeeDTO>>() {
                })
                .value(res ->{
                    EmployeeDTO employeeDTO1 = res.getData();
                    assertThat(employeeDTO1.getEmail()).isEqualTo(employeeDTO.getEmail());
                    assertThat(employeeDTO1.getAge()).isEqualTo(employeeDTO.getAge());
                });

        log.info("Employee is created successfully.");
    }

    @Test
    void NotCreateEmp_whenEmpIsPresentWithGivenEmail(){
        EmployeeEntity savedEmp = employeeRepository.save(employeeEntity);

        webTestClient.post()
                .uri("/employees")
                .bodyValue(employeeDTO)
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(new ParameterizedTypeReference<ApiResponse<EmployeeDTO>>() {
                })
                .value(res ->{
                    assertThat(res.getError()).isNotNull();
                    assertThat(res.getData()).isNull();
                });

        log.warn("Employee is already present with given email.");
    }

    @Test
    void UpdateEmp_whenEmpIsPresentWithGivenId(){
        EmployeeEntity savedEmp = employeeRepository.save(employeeEntity);

        EmployeeDTO update1 = EmployeeDTO.builder()
                .name("MD Swaley")
                .age(28)
                .role("ADMIN")
                .email(employeeEntity.getEmail())
                .dateOfJoining(LocalDateTime.now().toLocalDate())
                .salary(18000.00)
                .isActive(true).build();

        webTestClient.put()
                .uri("/employees/{id}",savedEmp.getId())
                .bodyValue(update1)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<ApiResponse<EmployeeDTO>>() {
                })
                .value(response ->{
                    assertThat(response.getData()).isNotNull();
                    assertThat(response.getData().getAge()).isEqualTo(28);
                    assertThat(response.getData().getSalary()).isEqualTo(18000.00);
                    assertThat(response.getData().getId()).isEqualTo(savedEmp.getId());
                });

        log.info("Updated emp of given ID.");

    }

    @Test
    void UpdateEmp_whenEmpIsNotPresentWithGivenId(){
        EmployeeDTO update1 = EmployeeDTO.builder()
                .name("MD Swaley")
                .age(28)
                .role("ADMIN")
                .email(employeeEntity.getEmail())
                .dateOfJoining(LocalDateTime.now().toLocalDate())
                .salary(18000.00)
                .isActive(true).build();

        webTestClient.put()
                .uri("/employees/{id}", employeeDTO.getId())
                .bodyValue(update1)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(new ParameterizedTypeReference<ApiResponse<EmployeeDTO>>() {
                })
                .value(res -> {
                    assertThat(res.getData()).isNull();
                    assertThat(res.getError()).isNotNull();
                });

        log.warn("Employee is Not Present with given ID for update.");
    }






}