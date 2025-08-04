package com.example.Enterprise_Resource_Planning.hr.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Enterprise_Resource_Planning.hr.model.Employee;
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findByDepartment(String department);
    List<Employee> findByStatus(String status);
    List<Employee> findByJobTitle(String jobTitle);
}
