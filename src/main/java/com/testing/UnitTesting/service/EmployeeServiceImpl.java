package com.testing.UnitTesting.service;

import com.testing.UnitTesting.exception.ResourceNotFoundException;
import com.testing.UnitTesting.model.Employee;
import com.testing.UnitTesting.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    //dependency injection using constructor
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> saveEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (saveEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exist with email: " + employee.getEmail());
        }
        else {
            return employeeRepository.save(employee);
        }
    }

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id){
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public String deleteEmployee(long id) {
        Optional<Employee> deleteEmployee = employeeRepository.findById(id);
        if (deleteEmployee.isPresent()){
            employeeRepository.deleteById(id);
            return "Employee deleted with id: "+id;
        }
        else{
            return "Employee ID "+id+" is not present to delete";
        }
    }


}
