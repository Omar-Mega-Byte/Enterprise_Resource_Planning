package com.example.Enterprise_Resource_Planning.inventory.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.Enterprise_Resource_Planning.inventory.model.ProductStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Complete response DTO for product information including audit fields.
 * Used for detailed product views and API responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    
    private Long id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private ProductStatus status;
    private Integer quantityInStock;
    private String category;
    private String supplier;
    private BigDecimal weightKg;
    private Integer minimumStockLevel;
    private Integer reorderPoint;
    
    // Audit fields
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;
    
    private String createdBy;
    private String lastModifiedBy;
    
    /**
     * Calculate total inventory value for this product
     */
    public BigDecimal getTotalValue() {
        if (unitPrice != null && quantityInStock != null) {
            return unitPrice.multiply(BigDecimal.valueOf(quantityInStock));
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Check if stock is below minimum level
     */
    public Boolean getBelowMinimumStock() {
        if (quantityInStock != null && minimumStockLevel != null) {
            return quantityInStock < minimumStockLevel;
        }
        return false;
    }
    
    /**
     * Check if product needs reordering
     */
    public Boolean getNeedsReorder() {
        if (quantityInStock != null && reorderPoint != null) {
            return quantityInStock <= reorderPoint;
        }
        return false;
    }
    
    /**
     * Determine stock status based on current levels
     */
    public String getStockStatus() {
        if (quantityInStock == null) {
            return "UNKNOWN";
        }
        
        if (quantityInStock == 0) {
            return "OUT_OF_STOCK";
        }
        
        if (getNeedsReorder()) {
            return "CRITICAL";
        }
        
        if (getBelowMinimumStock()) {
            return "LOW";
        }
        
        return "NORMAL";
    }
}
