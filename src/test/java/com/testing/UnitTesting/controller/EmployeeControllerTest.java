package com.testing.UnitTesting.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.UnitTesting.model.Employee;
import com.testing.UnitTesting.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.BDDMockito.*;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;                //Using mockMvc we will call REST Apis. Using MockMvc, we fakes HTTP requests for us, making it possible to run the controller tests without starting an entier HTTP server.

    @MockBean
    private EmployeeService employeeService;        //create a mock object of employee and register in application context, so that this mock obj can be available to employee controller

    @Autowired
    private ObjectMapper objectMapper;              //to serialize and deserailize java object

    //JUnit test for createEmployee method
    @DisplayName("JUnit test for createEmployee method")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Krishank")
                .lastName("Sarma")
                .email("krishank@gmail.com")
                .build();

        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/employee/addEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));                           //objectMapper convert the employee object to JSON

        //then - verify the record
        response.andDo(MockMvcResultHandlers.print())                                           //to trace the output of JUnit test case
                .andExpect(MockMvcResultMatchers.status().isCreated())                          //to verify if the response(ie the status code. which will be return by REST API) of the REST API is created or not
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",                //$ mean json object
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",                 //to verify response of the REST API contains a valid JSON value or not
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }

    //JUnit test for getAllEmployee method
    @DisplayName("JUnit test for getAllEmployee method")
    @Test
    public void givenEmployeeRequestForAll_whenGetAllEmployee_thenReturnEmployeeList() throws Exception {

        //given - precondition or setup
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add( Employee.builder()
                .firstName("Krishank")
                .lastName("Sarma")
                .email("krishank@gmail.com")
                .build());
        employeeList.add(Employee.builder()
                .firstName("Dinesh")
                .lastName("kumar")
                .email("kumar@mail.com")
                .build());
        given(employeeService.getAllEmployee()).willReturn(employeeList);

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employee/getAllEmployee"));

        //then - verify the record
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(employeeList.size())));
    }

    //JUnit test for getEmployeeById (positive scenario = valid employee id)
    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Krishank")
                .lastName("Sarma")
                .email("krishank@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employee/getEmployeeById/{id}", employee.getId()));

        //then - verify the record
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",                 //to verify response of the REST API contains a valid JSON value or not
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }

    //JUnit test for getEmployeeById (negative scenario = employeeId is not present)
    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturn404() throws Exception {           //404 for not found

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Krishank")
                .lastName("Sarma")
                .email("krishank@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.empty());          //passed empty employeeId

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employee/getEmployeeById/{id}", employee.getId()));

        //then - verify the record
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //JUnit test for updateEmployee (positive scenario = valid employee id)
    @DisplayName("JUnit test for updateEmployee (positive scenario = valid employee id)")
    @Test
    public void givenUpdateEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {

        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Krishank")
                .lastName("Sarma")
                .email("krishank@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Krish")
                .lastName("Saha")
                .email("krish@gmail.com")
                .build();

        given(employeeService.getEmployeeById(savedEmployee.getId())).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/employee/updateEmployee/{id}", savedEmployee.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the record
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(updatedEmployee.getEmail())));
    }

    //JUnit test for updateEmployee (negative scenario = invalid employee id)
    @DisplayName("JUnit test for updateEmployee (negative scenario = invalid employee id)")
    @Test
    public void givenInvalidUpdateEmployeeId_whenUpdateEmployee_thenReturn404() throws Exception {

        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Krishank")
                .lastName("Sarma")
                .email("krishank@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Krish")
                .lastName("Saha")
                .email("krish@gmail.com")
                .build();

        given(employeeService.getEmployeeById(savedEmployee.getId())).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/employee/updateEmployee/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the record
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //JUnit test for deleteEmployee
    @DisplayName("JUnit test for deleteEmployee")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {

        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Krishank")
                .lastName("Sarma")
                .email("krishank@gmail.com")
                .build();
        given(employeeService.deleteEmployee(savedEmployee.getId()))
                .willReturn(new ResponseEntity(HttpStatus.OK));

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/employee/deleteEmployee/{id}",savedEmployee.getId()));

        //then - verify the record
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
