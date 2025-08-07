package com.example.Enterprise_Resource_Planning.inventory.dto.request;

import java.math.BigDecimal;

import com.example.Enterprise_Resource_Planning.inventory.model.ProductStatus;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for updating an existing product.
 * All fields are optional to allow partial updates.
 */
@Data
public class ProductUpdateDTO {
    
    @Size(min = 3, max = 50, message = "SKU must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Z0-9-_]+$", message = "SKU can only contain uppercase letters, numbers, hyphens, and underscores")
    private String sku;
    
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    private String name;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @DecimalMin(value = "0.01", message = "Unit price must be greater than zero")
    @DecimalMax(value = "999999.99", message = "Unit price cannot exceed 999,999.99")
    @Digits(integer = 7, fraction = 2, message = "Unit price format is invalid")
    private BigDecimal unitPrice;
    
    private ProductStatus status;
    
    @Min(value = 0, message = "Quantity in stock cannot be negative")
    @Max(value = 1000000, message = "Quantity in stock cannot exceed 1,000,000")
    private Integer quantityInStock;
    
    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;
    
    @Size(max = 150, message = "Supplier cannot exceed 150 characters")
    private String supplier;
    
    @DecimalMin(value = "0.001", message = "Weight must be positive")
    @DecimalMax(value = "9999.999", message = "Weight cannot exceed 9999.999 kg")
    @Digits(integer = 4, fraction = 3, message = "Weight format is invalid")
    private BigDecimal weightKg;
    
    @Min(value = 0, message = "Minimum stock level cannot be negative")
    @Max(value = 100000, message = "Minimum stock level cannot exceed 100,000")
    private Integer minimumStockLevel;
    
    @Min(value = 0, message = "Reorder point cannot be negative")
    @Max(value = 100000, message = "Reorder point cannot exceed 100,000")
    private Integer reorderPoint;
    
    // Custom validation method
    @AssertTrue(message = "Reorder point should not exceed minimum stock level")
    public boolean isStockLevelsValid() {
        if (minimumStockLevel != null && reorderPoint != null) {
            return reorderPoint <= minimumStockLevel;
        }
        return true;
    }
}
