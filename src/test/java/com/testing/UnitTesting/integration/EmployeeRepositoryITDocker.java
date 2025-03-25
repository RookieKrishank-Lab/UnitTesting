package com.testing.UnitTesting.integration;
/*
* So in order to make sure that whatever the unit test cases we have written

is also working with respect to the real database are not for that

we're going to write the integration tests for the repository layer.
*
* */

import com.testing.UnitTesting.model.Employee;
import com.testing.UnitTesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)                    //this will disable the in-memory database support
public class EmployeeRepositoryITDocker extends AbstractionContainerBaseTest{


    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    //this annonation tell that whatever is inside this annotation run those things before excuiting each @Test
    public void setup(){
        employee = Employee.builder()
                .firstName("Krishn")
                .lastName("Sarma")
                .email("kri@mail.com")
                .build();
    }

    //Junit test for save Employee operation
    @DisplayName("Junit test for save Employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Krishn")
//                .lastName("Sarma")
//                .email("kri@mail.com")
//                .build();

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
//        Employee employee = Employee.builder()
//                .firstName("Krishn")
//                .lastName("Sarma")
//                .email("kri@mail.com")
//                .build();

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
        assertThat(employeeList.size()).isEqualTo(4);				//REASON for using 4=one extra as one default data already there in DB but while using docker image it will be 3 as we are just saving 3 employee data
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
        assertThat(employee1).isNotNull();
//        assertTrue(employee1.getEmail().equals("kri@mail1.com"));;
//        assertFalse(employee1.getEmail().equals("kri@mail.com"));
    }

    //JUnit test for get employee by email opearation
    @DisplayName("JUnit test for get employee by email opearation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Krishn")
                .lastName("Sarma")
                .email("kri@mail1.com")
                .build();

        employeeRepository.save(employee);

        //when - action or behaviour we are going to test
        if(employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            Employee employee1 = employeeRepository.findByEmail(employee.getEmail()).get();

            //then - verify the record
            assertThat(employee1).isNotNull();
        }
    }

    //JUnit test for employee update operation
    @DisplayName("JUnit test for employee update operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Krishn")
                .lastName("Sarma")
                .email("kri@mail1.com")
                .build();

        employeeRepository.save(employee);

        //when - action or behaviour we are going to test
        Employee saveEmployee = employeeRepository.findById(employee.getId()).get();
        saveEmployee.setFirstName("Krishank");
        Employee updatedEmployee = employeeRepository.save(saveEmployee);

        //then - verify the record
        assertThat(updatedEmployee.getFirstName()).isEqualTo(saveEmployee.getFirstName());
    }

    //JUnit test for delete operation
    @DisplayName("JUnit test for delete operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Krishn")
                .lastName("Sarma")
                .email("kri@mail1.com")
                .build();

        employeeRepository.save(employee);

        //when - action or behaviour we are going to test
        employeeRepository.deleteById(employee.getId());

        //then - verify the record
        Optional<Employee> deletedEmployee = employeeRepository.findById(employee.getId());//
        assertThat(deletedEmployee).isEmpty();
    }

    //JUnit test for custom query using JPQL with index
    @DisplayName("JUnit test for custom query using JPQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJQPL_thenReturnEmployeeObject() {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Krishn")
                .lastName("Sarma")
                .email("kri@mail1.com")
                .build();

        employeeRepository.save(employee);

        //when - action or behaviour we are going to test
        Employee savedEmployee = employeeRepository.findByJPQL(employee.getFirstName(),employee.getLastName());

        //then - verify the record
        System.out.println(savedEmployee.getFirstName()+" "+savedEmployee.getLastName());
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for custom query using JPQL with named paramaters
    @DisplayName("JUnit test for custom query using JPQL with named paramaters")
    @Test
    public void givenFirstNameAndLastName_whenFindByJQPLNamedParams_thenReturnEmployeeObject() {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Krishank")
                .lastName("Sarma")
                .email("kri@mail1.com")
                .build();
        employeeRepository.save(employee);
        String firstName="Krishank";
        String lastName="Sarma";

        //when - action or behaviour we are going to test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName,lastName);

        //then - verify the record
        System.out.println("Repo value: "+employee.getFirstName()+" "+employee.getLastName());
        System.out.println("Passed argument value: "+firstName+" "+lastName);
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for custom query using native SQL with index
    @DisplayName("JUnit test for custom query using native SQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnObject() {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Krishn")
                .lastName("Sarma")
                .email("kri@mail1.com")
                .build();
        employeeRepository.save(employee);
        String firstName="Krishn";
        String lastName="Sarma";

        //when - action or behaviour we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQL(firstName, lastName);

        //then - verify the record
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for custom query using native SQL with named params
    @DisplayName("JUnit test for custom query using native SQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail,com")
                .build();
        employeeRepository.save(employee);
        // String firstName = "Ramesh";
        // String lastName = "Fadatare";

        // when -  action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamed(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }
}
