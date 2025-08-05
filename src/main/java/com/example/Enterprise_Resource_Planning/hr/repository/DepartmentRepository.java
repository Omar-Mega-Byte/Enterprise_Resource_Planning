package com.example.Enterprise_Resource_Planning.hr.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Enterprise_Resource_Planning.hr.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
}
