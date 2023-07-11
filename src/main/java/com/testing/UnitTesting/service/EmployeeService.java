package com.testing.UnitTesting.service;

import com.testing.UnitTesting.model.Employee;
import com.testing.UnitTesting.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EmployeeService {

    public Employee saveEmployee(Employee employee);
    public List<Employee> getAllEmployee();
    public Optional<Employee> getEmployeeById(long id);
    public Employee updateEmployee(Employee employee);
    public ResponseEntity<String> deleteEmployee(long id);
}
