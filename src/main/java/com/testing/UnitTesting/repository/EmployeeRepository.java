package com.testing.UnitTesting.repository;

import com.testing.UnitTesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    //define custom query using JPQL with index parameters
    @Query("select e from Employee e where e.firstName=?1 and e.lastName=?2")           //?=placeholder, and number is the index, in JPQL we use class name i.e Employee. e is the alias for Employee
    Employee findByJPQL(String firstName, String lastName);

    //define custom query using JPQL with named parameters
    // This code snippet defines a JPQL query that selects an Employee entity based on their first name and last name using named parameters.
    // This code snippet defines a method that uses a JPQL query to find an Employee entity by their first name and last name, using named parameters.

    @Query("select e from Employee e where e.firstName=:firstName and e.lastName=:lastName")
    Employee findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    //define custom query using native SQL with index parameters
    @Query(value = "select * from Employees e where e.first_name=?1 and e.last_name=?2", nativeQuery = true)        //instead of class name we are using table name here it is Employees. nativeQuery = true tell SpringJPA that we are using native SQL query
    Employee findByNativeSQL(String firstName, String lastName);

    // define custom query using Native SQL with named params
    @Query(value = "select * from employees e where e.first_name =:firstName and e.last_name =:lastName",
            nativeQuery = true)
    Employee findByNativeSQLNamed(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
