package com.example.employeedata;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class EmployeeDataApplicationTest {

	@Test
	void contextLoads() {
        log.info("Spring Boot context loaded successfully");
	}


    @BeforeEach
    void beforeCall(){
        log.info("Before Each method is calling.");
    }

    @AfterEach
    void afterCall(){
        log.info("After Each method is calling.");
    }
}
