package com.testing.UnitTesting.service;

import com.testing.UnitTesting.exception.ResourceNotFoundException;
import com.testing.UnitTesting.model.Employee;
import com.testing.UnitTesting.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .id(1L)
                .firstName("Krishank")
                .lastName("Sarma")
                .email("krishank@gmail.com")
                .build();
//        employeeRepository = Mockito.mock(EmployeeRepository.class);                                  //here we have mock the dependency using mock method
//        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    //JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))                      //here inside Service we have saveEmployee method, which have 2 methods findByEmail and save method so we to stub these 2 methods(i.e mock these 2 methods) and then provide a configure response
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or behaviour we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);                //in this step we have test the saveEmployee method

        //then - verify the record
        assertThat(savedEmployee).isNotNull();

        //Now print the object
        System.out.println(employeeRepository);
        System.out.println(employeeService);
        System.out.println(savedEmployee.toString());

    }

    //JUnit test for saveEmployee method which throws exception
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {

        //given - precondition or setup

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

//        given(employeeRepository.save(employee)).willReturn(employee);                      //after throwing the custom exception we dont need to save the object in save method so we dont need to stub save method

        //when - action or behaviour we are going to test
        Assertions.assertThrows(ResourceNotFoundException.class, ()->employeeService.saveEmployee(employee));                        //the way to handle the exception

        //then - verify the record
       verify(employeeRepository, never()).save(any(Employee.class));

        //Now print the object
        System.out.println(employeeRepository);
        System.out.println(employeeService);
    }

    //JUnit test for getAllEmployee method
    @DisplayName("JUnit test for getAllEmployee method")
    @Test
    public void givenEmployeeList_whenGetAllEmployee_thenReturnEmployeeList() {

        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Dinesh")
                .lastName("kumar")
                .email("kumar@mail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Rahul")
                .lastName("Rahul")
                .email("Rahul@mail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee,employee1,employee2));

        //when - action or behaviour we are going to test
        List<Employee> employeeList = employeeService.getAllEmployee();

        //then - verify the record
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(3);
    }

    //JUnit test for getAllEmployee method (negative scenario)
    @DisplayName("JUnit test for getAllEmployee method (negative scenario)")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployee_thenReturnEmptyEmployeeList() {

        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Dinesh")
                .lastName("kumar")
                .email("kumar@mail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Rahul")
                .lastName("Rahul")
                .email("Rahul@mail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or behaviour we are going to test
        List<Employee> employeeList = employeeService.getAllEmployee();

        //then - verify the record
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    //JUnit test for getEmployeeById
    @DisplayName("JUnit test for getEmployeeById")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {

        //given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when - action or behaviour we are going to test
        Employee uniqueEmployee = employeeService.getEmployeeById(employee.getId()).get();

        //then - verify the record
        assertThat(uniqueEmployee).isNotNull();
    }

    //JUnit test for updateEmployee method
    @DisplayName("JUnit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmployeeObject() {

        //given - precondition or setup
        employee.setEmail("ram@gmail.com");
        employee.setFirstName("Ram");
        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or behaviour we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then - verify the record
        assertThat(employee.getFirstName()).isEqualTo("Ram");
        assertThat(employee.getEmail()).isEqualTo("ram@gmail.com");
        System.out.println("Updated values: "+employee.getFirstName()+"and"+employee.getEmail());
    }

    //JUnit test for deleteEmployee method
    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenDeleteThatEmployeeId() {

        //given - precondition or setup
        given(employeeRepository.findById(employee.getId())).willReturn()

        //when - action or behaviour we are going to test

        //then - verify the record
    }
}
