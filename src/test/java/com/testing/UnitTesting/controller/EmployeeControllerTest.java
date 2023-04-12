package com.testing.UnitTesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.UnitTesting.model.Employee;
import com.testing.UnitTesting.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;                //Using mockMvc we will call REST Apis

    @MockBean
    private EmployeeService employeeService;        //create a mock object of employee and register in application context, so that this mock obj can be available to employee controller

    @Autowired
    private ObjectMapper objectMapper;

    //JUnit test for createEmployee method
    @DisplayName("JUnit test for createEmployee method")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployeeObject() {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .id(8L)
                .firstName("Krishank")
                .lastName("Sarma")
                .email("krishank@gmail.com")
                .build();

        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer()
        //when - action or behaviour we are going to test

        //then - verify the record
    }
}
