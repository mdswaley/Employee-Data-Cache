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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@Import(EmployeeTestContainer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@Slf4j
class EmployeeServiceTest {

    private EmployeeEntity employeeEntity;

    private EmployeeDTO employeeDTO;

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp(){
        employeeEntity = EmployeeEntity.builder()
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
        log.error("Employee is not present with id 1");
    }

    @Test
    void GetAllEmp_whenEmpIsPresent(){
        when(employeeRepository.findAll()).thenReturn(List.of(employeeEntity));
        List<EmployeeDTO> getEmp = employeeService.getAllEmployees();

        assertThat(getEmp).isNotNull();
        assertThat(getEmp).isNotEmpty();
        assertThat(getEmp.size()).isEqualTo(1);
        log.info("Employees are present.");
    }

    @Test
    void GetAllEmp_whenEmpIsNotPresent(){
        when(employeeRepository.findAll()).thenReturn(List.of());
        List<EmployeeDTO> getEmp = employeeService.getAllEmployees();

        assertThat(getEmp).isEmpty();
        log.error("Employees are not present.");
    }

    @Test
    void CreateEmp_whenEmpIsNotPresentWithGivenEmail(){
        when(employeeRepository.existsByEmail(employeeDTO.getEmail())).thenReturn(false);

        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);

        EmployeeDTO saveEmp = employeeService.createNewEmployee(employeeDTO);

        assertThat(saveEmp).isNotNull();
        assertThat(saveEmp.getEmail()).isEqualTo(employeeDTO.getEmail());

        verify(employeeRepository).existsByEmail(employeeDTO.getEmail());
        log.info("Employee is created with email {}", employeeDTO.getEmail());
    }

    @Test
    void CreateEmp_whenEmpISPresentWithGivenEmail(){
        when(employeeRepository.existsByEmail(employeeDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(()->employeeService.isExistEmpByEmail(employeeDTO.getEmail()))
                .isInstanceOf(RuntimeException.class).hasMessage("Employee is already Present with email md123@gmail.com");

        log.error("Employee is already present with email {}", employeeDTO.getEmail());
    }

    @Test
    void UpdateEmp_whenEmpIsPresentWithGivenId(){
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeDTO.setId(1L);
        employeeDTO.setAge(24);
        employeeDTO.setRole("USER");

        EmployeeEntity employeeEntity1 = modelMapper.map(employeeDTO,EmployeeEntity.class);

        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity1);

        EmployeeDTO updateEmp = employeeService.updateEmployeeById(employeeDTO.getId(), employeeDTO);

        assertThat(updateEmp).isEqualTo(employeeDTO);
        verify(employeeRepository).save(any(EmployeeEntity.class));
        verify(employeeRepository).existsById(1L);
        log.info("Employee is updated successfully.");
    }

    @Test
    void UpdateEmp_whenEmpIsNotPresentWithGivenId(){
        Long id = 1L;
        when(employeeRepository.existsById(id)).thenReturn(false);
        assertThatThrownBy(()->employeeService.updateEmployeeById(1L, employeeDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        log.info("Employee is not present for update.");
    }

    @Test
    void DeleteEmp_whenEmpIsNotPresentWithGivenId(){
        when(employeeRepository.existsById(1L)).thenReturn(false);
        assertThatThrownBy(()->employeeService.deleteEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");
        log.error("Employee is present to delete with id : 1");
    }

    @Test
    void DeleteEmp_whenEmpIsPresentWithGivenId(){
        Long id = 1L;
        when(employeeRepository.existsById(id)).thenReturn(true);

        assertThatCode(()->employeeService.deleteEmployeeById(id))
                .doesNotThrowAnyException();

        verify(employeeRepository).existsById(id);
        log.info("Employee is Deleted successfully with id : 1");
    }

    @Test
    void UpdateEmpPartially_whenEmpIsPresentWithGivenId(){
        Long id = 1L;
        Map<String, Object> updated = new HashMap<>();

        updated.put("name","Swaley");
        updated.put("age", 23);
        updated.put("salary", 20000.00);

        when(employeeRepository.existsById(id)).thenReturn(true);
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employeeEntity));

        when(employeeRepository.save(any(EmployeeEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
//      thenAnswer() ->
//      Whatever object you pass into save(…), return that exact same object.”
//
//      Not a copy.
//      Not a new object.
//      The same reference.

        EmployeeDTO updatedEmp = employeeService.updatePartialEmployeeById(id, updated);

        assertThat(updatedEmp).isNotNull();
        assertThat(updatedEmp.getName()).isEqualTo("Swaley");
        assertThat(updatedEmp.getAge()).isEqualTo(23);
        assertThat(updatedEmp.getSalary()).isEqualTo(20000.00);

        verify(employeeRepository).existsById(id);
        verify(employeeRepository).findById(id);
        verify(employeeRepository).save(employeeEntity);

        log.info("Employee is updated partially.");
    }


    @Test
    void UpdateEmpPartially_whenEmpIsNotPresentWithGivenId(){
        Long id = 1L;
        Map<String, Object> update = new HashMap<>();
        when(employeeRepository.existsById(id)).thenReturn(false);
//        this will not execute after existsById returns false. So that's why there will a error -> "UnnecessaryStubbingException"
//        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(()->employeeService.updatePartialEmployeeById(1L, update))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        log.error("Employee is not present with given id for partially update.");
    }
}