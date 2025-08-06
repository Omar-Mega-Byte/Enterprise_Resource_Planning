package com.example.Enterprise_Resource_Planning.inventory.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.example.Enterprise_Resource_Planning.common.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base auditable entity that provides audit trail functionality.
 * Extends BaseEntity and adds user tracking and optimistic locking.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class AuditableEntity extends BaseEntity {
    
    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 100)
    private String lastModifiedBy;

    @Version
    @Column(name = "version")
    private Long version;
}
