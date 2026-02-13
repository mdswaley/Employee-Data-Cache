package com.example.employeedata.dto;

import com.example.employeedata.annotations.EmployeeRoleValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO implements Serializable {
    private Long id;
    private String name;
    private String email;

    @Min(value = 18,message = "age should be greater then 18")
    private Integer age;

    @EmployeeRoleValidation
    private String role; //ADMIN, USER
    private Double salary;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfJoining;
    private Boolean isActive;
}
