package com.example.Enterprise_Resource_Planning.hr.model;
import com.example.Enterprise_Resource_Planning.common.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
public class Department extends BaseEntity {
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 100)
    private String location;
}
