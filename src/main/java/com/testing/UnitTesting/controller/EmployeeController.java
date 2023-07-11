package com.testing.UnitTesting.controller;

import com.testing.UnitTesting.model.Employee;
import com.testing.UnitTesting.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    private EmployeeController(EmployeeService employeeService){
        this.employeeService=employeeService;
    }

    @PostMapping("/addEmployee")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
    }

    @GetMapping("/getAllEmployee")
    public List<Employee> getAllEmployee(){
        return employeeService.getAllEmployee();
    }

    @GetMapping("/getEmployeeById/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long employeeId){
        return employeeService.getEmployeeById(employeeId)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());                  //.build() is used to buld an object
    }

    @PutMapping("/updateEmployee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long employeeId, @RequestBody Employee employee){
        return employeeService.getEmployeeById(employeeId)
                .map(saveEmployee ->{
                    saveEmployee.setFirstName(employee.getFirstName());
                    saveEmployee.setLastName(employee.getLastName());
                    saveEmployee.setEmail(employee.getEmail());

                    Employee updateEmployee = employeeService.updateEmployee(saveEmployee);
                    return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
                })
                .orElseGet(()-> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/deleteEmployee/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long employeeId){
        return employeeService.deleteEmployee(employeeId);

    }
}
