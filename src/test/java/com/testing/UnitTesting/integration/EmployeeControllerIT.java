package com.testing.UnitTesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.UnitTesting.model.Employee;
import com.testing.UnitTesting.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)             //for integrating testing
@AutoConfigureMockMvc
@Testcontainers
public class EmployeeControllerIT {

    @Container
    private static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }

    //JUnit test for createEmployee method
    @DisplayName("JUnit test for createEmployee method")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployeeObject() throws Exception{
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Krishank")
                .lastName("Sarma")
                .email("krishank@gmail.com")
                .build();

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
        employeeRepository.saveAll(employeeList);

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
        employeeRepository.save(employee);

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
        long employeeId = 8L;
        Employee employee = Employee.builder()
                .firstName("Krishank")
                .lastName("Sarma")
                .email("krishank@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employee/getEmployeeById/{id}", employeeId));

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
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Krish")
                .lastName("Saha")
                .email("krish@gmail.com")
                .build();



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
        long employeeId = 100L;
        Employee savedEmployee = Employee.builder()
                .firstName("Krishank")
                .lastName("Sarma")
                .email("krishank@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Krish")
                .lastName("Saha")
                .email("krish@gmail.com")
                .build();

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/employee/updateEmployee/{id}", employeeId)
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
        employeeRepository.save(savedEmployee);

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/employee/deleteEmployee/{id}",savedEmployee.getId()));

        //then - verify the record
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
