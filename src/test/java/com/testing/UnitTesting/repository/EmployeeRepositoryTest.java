package com.testing.UnitTesting.repository;

import com.testing.UnitTesting.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    //Junit test for save Employee operation
    @DisplayName("Junit test for save Employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Krishn")
                .lastName("Sarma")
                .email("kri@mail.com")
                .build();

        //when - action or behaviour we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the record
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //JUnit test for get all employee operation
    @DisplayName("JUnit test for get all employee operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList(){

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Krishn")
                .lastName("Sarma")
                .email("kri@mail.com")
                .build();

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

        employeeRepository.save(employee);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        //when - action or behaviour we are going to test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the record
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(3);
    }

    //JUnit test for get employee by id operation
    @DisplayName("givenEmployeeObject_whenFindById_thenReturnEmployeeObject")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Krishn")
                .lastName("Sarma")
                .email("kri@mail1.com")
                .build();

        employeeRepository.save(employee);

        //when - action or behaviour we are going to test
        Employee employee1 = employeeRepository.findById(employee.getId()).get();

        //then - verify the record
//        assertThat(employee1 !=null).isNotNull();
        assertTrue(employee1.getEmail().equals("kri@mail1.com"));;
        assertFalse(employee1.getEmail().equals("kri@mail.com"));
    }

}
