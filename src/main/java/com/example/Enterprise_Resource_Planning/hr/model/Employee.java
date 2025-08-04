package com.example.Enterprise_Resource_Planning.hr.model;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.Enterprise_Resource_Planning.common.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 50)
    private String nationalId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Column(nullable = false, length = 100)
    private String jobTitle;

    @Column(nullable = false, length = 50)
    private String department;

    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    @Column(length = 10)
    private String status; 

    @Column
    private String address;

    @Column
    private String notes;

}
