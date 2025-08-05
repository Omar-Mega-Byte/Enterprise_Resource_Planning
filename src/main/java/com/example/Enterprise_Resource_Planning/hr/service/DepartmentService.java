package com.example.Enterprise_Resource_Planning.hr.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Enterprise_Resource_Planning.hr.model.Department;
import com.example.Enterprise_Resource_Planning.hr.repository.DepartmentRepository;
@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
    
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Department findById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    public Department updateDepartment(Long id, Department department) {
        if (departmentRepository.existsById(id)) {
            department.setId(id);
            return departmentRepository.save(department);
        }
        return null;
    }
}
