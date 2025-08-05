package com.example.Enterprise_Resource_Planning.hr.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Enterprise_Resource_Planning.hr.model.Department;
import com.example.Enterprise_Resource_Planning.hr.model.Employee;
import com.example.Enterprise_Resource_Planning.hr.model.EmployeeStatus;
import com.example.Enterprise_Resource_Planning.hr.model.JobTitle;
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findByStatus(EmployeeStatus status);
    List<Employee> findByJobTitle(JobTitle jobTitle);
    List<Employee> findByDepartment(Department department);
}
