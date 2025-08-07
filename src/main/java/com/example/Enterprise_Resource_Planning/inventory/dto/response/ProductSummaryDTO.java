package com.example.Enterprise_Resource_Planning.inventory.dto.response;

import java.math.BigDecimal;

import com.example.Enterprise_Resource_Planning.inventory.model.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Minimal response DTO for product summaries.
 * Used for list operations and search results to reduce payload size.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryDTO {
    
    private Long id;
    private String sku;
    private String name;
    private BigDecimal unitPrice;
    private ProductStatus status;
    private Integer quantityInStock;
    private String category;
    
    // Quick status indicators
    private Boolean needsReorder;
    private String stockStatus; // "NORMAL", "LOW", "CRITICAL", "OUT_OF_STOCK"
    
    /**
     * Check if product needs reordering based on quantity and reorder point
     */
    public Boolean getNeedsReorder() {
        // This would be set during mapping from the full entity
        return needsReorder;
    }
    
    /**
     * Get simplified stock status
     */
    public String getStockStatus() {
        if (quantityInStock == null) {
            return "UNKNOWN";
        }
        
        if (quantityInStock == 0) {
            return "OUT_OF_STOCK";
        }
        
        if (Boolean.TRUE.equals(needsReorder)) {
            return "CRITICAL";
        }
        
        // For summary, we simplify - service layer will calculate these
        return stockStatus != null ? stockStatus : "NORMAL";
    }
}
