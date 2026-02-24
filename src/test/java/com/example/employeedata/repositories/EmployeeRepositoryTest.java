package com.example.employeedata.repositories;

import com.example.employeedata.EmployeeTestContainer;
import com.example.employeedata.dto.EmployeeDTO;
import com.example.employeedata.entities.EmployeeEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.w3c.dom.stylesheets.LinkStyle;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;

@Import(EmployeeTestContainer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Slf4j
class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeEntity employeeEntity;

    @BeforeEach
    void setUp(){
        employeeEntity = EmployeeEntity.builder()
                .name("MD Swaley")
                .age(22)
                .role("ADMIN")
                .email("md123@gmail.com")
                .dateOfJoining(LocalDateTime.now().toLocalDate())
                .salary(10000.00)
                .isActive(true)
                .build();
    }

    @Test
    void ExistByEmail_whenGivenEmailIsPresent(){
        employeeRepository.save(employeeEntity);

        Boolean emp = employeeRepository.existsByEmail(employeeEntity.getEmail());

        assertThat(emp).isTrue();
       log.info("Employee is Exist with given email.");
    }

    @Test
    void ExistByEmail_whenGivenEmailIsNotPresent(){
        Boolean emp = employeeRepository.existsByEmail(employeeEntity.getEmail());
        assertThat(emp).isFalse();
        log.error("Employee is Not Exist with given email.");
    }




}