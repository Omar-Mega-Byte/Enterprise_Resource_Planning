package com.example.Enterprise_Resource_Planning.inventory.model;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Product entity representing inventory items.
 * Extends AuditableEntity for audit trail functionality.
 */
@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_product_sku", columnList = "sku", unique = true),
    @Index(name = "idx_product_name", columnList = "name"),
    @Index(name = "idx_product_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = true)
public class Product extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "sku", unique = true, nullable = false, length = 50)
    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 50, message = "SKU must be between 3 and 50 characters")
    private String sku;
    
    @Column(name = "name", nullable = false, length = 200)
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    private String name;

    @Column(name = "description", length = 1000)
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Column(name = "unit_price", precision = 19, scale = 2, nullable = false)
    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.00", message = "Unit price must be non-negative")
    @DecimalMax(value = "999999.99", message = "Unit price cannot exceed 999,999.99")
    private BigDecimal unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull(message = "Product status is required")
    private ProductStatus status = ProductStatus.AVAILABLE;

    @Column(name = "quantity_in_stock", nullable = false)
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantityInStock = 0;

    @Column(name = "category", length = 100)
    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;

    @Column(name = "supplier", length = 150)
    @Size(max = 150, message = "Supplier cannot exceed 150 characters")
    private String supplier;

    @Column(name = "weight_kg", precision = 10, scale = 3)
    @DecimalMin(value = "0.001", message = "Weight must be positive")
    private BigDecimal weightKg;
    
    @Column(name = "minimum_stock_level")
    @Min(value = 0, message = "Minimum stock level cannot be negative")
    private Integer minimumStockLevel = 0;
    
    @Column(name = "reorder_point")
    @Min(value = 0, message = "Reorder point cannot be negative")
    private Integer reorderPoint = 0;
    
    public boolean isLowStock() {
        return quantityInStock != null && minimumStockLevel != null && 
            quantityInStock <= minimumStockLevel;
    }
    
    public boolean needsReorder() {
        return quantityInStock != null && reorderPoint != null && 
            quantityInStock <= reorderPoint;
    }
    
    public boolean isInStock() {
        return quantityInStock != null && quantityInStock > 0;
    }
}
