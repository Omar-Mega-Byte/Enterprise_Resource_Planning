package com.example.Enterprise_Resource_Planning.hr.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Enterprise_Resource_Planning.hr.exception.EmployeeNotFoundException;
import com.example.Enterprise_Resource_Planning.hr.exception.InvalidEmployeeException;
import com.example.Enterprise_Resource_Planning.hr.model.Department;
import com.example.Enterprise_Resource_Planning.hr.model.Employee;
import com.example.Enterprise_Resource_Planning.hr.repository.EmployeeRepository;
import com.example.Enterprise_Resource_Planning.hr.validation.EmployeeValidator;

/**
 * Service class for managing Employee entities.
 * Provides business logic for CRUD operations on employees.
 * 
 * All exceptions are designed to be handled by the GlobalExceptionHandler.
 */
@Service
@Transactional
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final EmployeeValidator employeeValidator;

    /**
     * Constructor for EmployeeService.
     * 
     * @param employeeRepository the employee repository
     * @param employeeValidator the employee validator
     */
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeValidator employeeValidator) {
        this.employeeRepository = employeeRepository;
        this.employeeValidator = employeeValidator;
    }

    /**
     * Creates a new employee.
     * 
     * @param employee the employee to create
     * @return the created employee
     * @throws InvalidEmployeeException if employee validation fails
     */
    public Employee createEmployee(Employee employee) {
        employeeValidator.validateEmployee(employee);
        return employeeRepository.save(employee);
    }

    /**
     * Retrieves all employees.
     * 
     * @return List of all employees
     */
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    /**
     * Finds an employee by its ID.
     * 
     * @param id the employee ID
     * @return the employee if found
     * @throws InvalidEmployeeException if id is null
     * @throws EmployeeNotFoundException if employee is not found
     */
    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        employeeValidator.validateEmployeeId(id);
        return employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    /**
     * Finds an employee by email address.
     * 
     * @param email the employee email
     * @return the employee if found
     * @throws InvalidEmployeeException if email is null or empty
     * @throws EmployeeNotFoundException if employee is not found
     */
    @Transactional(readOnly = true)
    public Employee findByEmail(String email) {
        employeeValidator.validateEmail(email);
        return employeeRepository.findByEmail(email)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with email: " + email));
    }

    /**
     * Finds all employees in a specific department.
     * 
     * @param department the department to search in
     * @return List of employees in the department
     * @throws InvalidEmployeeException if department is null
     */
    @Transactional(readOnly = true)
    public List<Employee> findByDepartment(Department department) {
        if (department == null) {
            throw new InvalidEmployeeException("Department cannot be null");
        }
        return employeeRepository.findByDepartment(department);
    }

    /**
     * Updates an existing employee.
     * 
     * @param id the ID of the employee to update
     * @param employee the updated employee data
     * @return the updated employee
     * @throws InvalidEmployeeException if parameters are invalid
     * @throws EmployeeNotFoundException if employee doesn't exist
     */
    public Employee updateEmployee(Long id, Employee employee) {
        employeeValidator.validateEmployeeId(id);
        employeeValidator.validateEmployee(employee);
        
        return employeeRepository.findById(id)
                .map(existingEmployee -> {
                    employee.setId(id);
                    return employeeRepository.save(employee);
                })
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " does not exist"));
    }

    /**
     * Deletes an employee by ID.
     * 
     * @param id the ID of the employee to delete
     * @return true if deletion was successful
     * @throws InvalidEmployeeException if id is null
     * @throws EmployeeNotFoundException if employee doesn't exist
     */
    public Boolean deleteById(Long id) {
        employeeValidator.validateEmployeeId(id);
        
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        
        employeeRepository.delete(employee);
        return true;
    }
}