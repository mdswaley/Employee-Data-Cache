package com.example.employeedata.services;

import com.example.employeedata.EmployeeTestContainer;
import com.example.employeedata.dto.EmployeeDTO;
import com.example.employeedata.entities.EmployeeEntity;
import com.example.employeedata.exceptions.ResourceNotFoundException;
import com.example.employeedata.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@Import(EmployeeTestContainer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@Slf4j
class EmployeeServiceTest {

    @Mock
    private EmployeeEntity employeeEntity;

    @Mock
    private EmployeeDTO employeeDTO;

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp(){
        employeeEntity.builder()
                .id(1L)
                .name("MD Swaley")
                .age(22)
                .role("ADMIN")
                .email("md123@gmail.com")
                .dateOfJoining(LocalDateTime.now().toLocalDate())
                .salary(10000.00)
                .isActive(true)
                .build();

        employeeDTO = modelMapper.map(employeeEntity, EmployeeDTO.class);

    }

    @Test
    void GetEmpById_whenEmpIsPresent_thenReturnEmp() {
        Long id = 1L;

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employeeEntity));

        EmployeeDTO employeeDTO1 = employeeService.getEmployeeById(id);
        assertThat(employeeDTO1).isNotNull();
        assertThat(employeeDTO1.getId()).isEqualTo(employeeDTO.getId());
        assertThat(employeeDTO1.getEmail()).isEqualTo(employeeDTO.getEmail());
        verify(employeeRepository).findById(id);

        verify(employeeRepository, times(1)).findById(id);
        log.info("Employee is present with id : {}", id);
    }

    @Test
    void GetEmpById_whenEmpIsNotPresent_thenThrowException(){
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee is not present with id=1");
    }





}