package com.example.Enterprise_Resource_Planning.inventory.dto.request;

import java.math.BigDecimal;

import com.example.Enterprise_Resource_Planning.inventory.model.ProductStatus;

import lombok.Data;

/**
 * DTO for search criteria when querying products.
 * All fields are optional for flexible search combinations.
 */
@Data
public class ProductSearchCriteria {
    
    private String sku;
    private String nameContaining;
    private String descriptionContaining;
    
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    
    private ProductStatus status;
    
    private Integer minQuantity;
    private Integer maxQuantity;
    
    private String category;
    private String supplier;
    
    private BigDecimal minWeight;
    private BigDecimal maxWeight;
    
    // Stock level filters
    private Boolean belowMinimumStock; // Find products with quantity < minimumStockLevel
    private Boolean needsReorder; // Find products with quantity <= reorderPoint
    
    // Sorting options
    private String sortBy; // "name", "sku", "unitPrice", "quantityInStock", "createdDate"
    private String sortDirection; // "ASC" or "DESC"
    
    // Pagination
    private Integer page = 0;
    private Integer size = 20;
    
    /**
     * Check if any search criteria is provided
     */
    public boolean hasSearchCriteria() {
        return sku != null || nameContaining != null || descriptionContaining != null ||
                minPrice != null || maxPrice != null || status != null ||
                minQuantity != null || maxQuantity != null ||
                category != null || supplier != null ||
                minWeight != null || maxWeight != null ||
                (belowMinimumStock != null && belowMinimumStock) ||
                (needsReorder != null && needsReorder);
        }
    
    /**
     * Validate price range
     */
    public boolean isPriceRangeValid() {
        if (minPrice != null && maxPrice != null) {
            return minPrice.compareTo(maxPrice) <= 0;
        }
        return true;
    }
    
    /**
     * Validate quantity range
     */
    public boolean isQuantityRangeValid() {
        if (minQuantity != null && maxQuantity != null) {
            return minQuantity <= maxQuantity;
        }
        return true;
    }
    
    /**
     * Validate weight range
     */
    public boolean isWeightRangeValid() {
        if (minWeight != null && maxWeight != null) {
            return minWeight.compareTo(maxWeight) <= 0;
        }
        return true;
    }
}
