package com.example.Enterprise_Resource_Planning.inventory.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.Enterprise_Resource_Planning.inventory.dto.request.ProductCreateDTO;
import com.example.Enterprise_Resource_Planning.inventory.dto.request.ProductUpdateDTO;
import com.example.Enterprise_Resource_Planning.inventory.dto.response.ProductResponseDTO;
import com.example.Enterprise_Resource_Planning.inventory.dto.response.ProductSummaryDTO;
import com.example.Enterprise_Resource_Planning.inventory.model.Product;

/**
 * Mapper for converting between Product entities and DTOs.
 * Essential for clean separation between data layer and API layer.
 */
@Component
public class ProductMapper {

    /**
     * Convert ProductCreateDTO to Product entity for creation
     */
    public Product toEntity(ProductCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setSku(dto.getSku());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setUnitPrice(dto.getUnitPrice());
        // Status will be set to a default value in the service layer
        product.setQuantityInStock(dto.getQuantityInStock());
        product.setCategory(dto.getCategory());
        product.setSupplier(dto.getSupplier());
        product.setWeightKg(dto.getWeightKg());
        product.setMinimumStockLevel(dto.getMinimumStockLevel());
        product.setReorderPoint(dto.getReorderPoint());
        
        return product;
    }

    /**
     * Update existing Product entity with ProductUpdateDTO data
     * Only updates non-null fields (partial update)
     */
    public void updateEntity(Product existingProduct, ProductUpdateDTO dto) {
        if (dto == null || existingProduct == null) {
            return;
        }

        if (dto.getSku() != null) {
            existingProduct.setSku(dto.getSku());
        }
        if (dto.getName() != null) {
            existingProduct.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            existingProduct.setDescription(dto.getDescription());
        }
        if (dto.getUnitPrice() != null) {
            existingProduct.setUnitPrice(dto.getUnitPrice());
        }
        if (dto.getStatus() != null) {
            existingProduct.setStatus(dto.getStatus());
        }
        if (dto.getQuantityInStock() != null) {
            existingProduct.setQuantityInStock(dto.getQuantityInStock());
        }
        if (dto.getCategory() != null) {
            existingProduct.setCategory(dto.getCategory());
        }
        if (dto.getSupplier() != null) {
            existingProduct.setSupplier(dto.getSupplier());
        }
        if (dto.getWeightKg() != null) {
            existingProduct.setWeightKg(dto.getWeightKg());
        }
        if (dto.getMinimumStockLevel() != null) {
            existingProduct.setMinimumStockLevel(dto.getMinimumStockLevel());
        }
        if (dto.getReorderPoint() != null) {
            existingProduct.setReorderPoint(dto.getReorderPoint());
        }
    }

    /**
     * Convert Product entity to ProductResponseDTO for API responses
     */
    public ProductResponseDTO toResponseDTO(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponseDTO.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .status(product.getStatus())
                .quantityInStock(product.getQuantityInStock())
                .category(product.getCategory())
                .supplier(product.getSupplier())
                .weightKg(product.getWeightKg())
                .minimumStockLevel(product.getMinimumStockLevel())
                .reorderPoint(product.getReorderPoint())
                .createdDate(product.getCreatedAt())
                .lastModifiedDate(product.getUpdatedAt())
                .createdBy(product.getCreatedBy())
                .lastModifiedBy(product.getLastModifiedBy())
                .build();
    }

    /**
     * Convert Product entity to ProductSummaryDTO for list operations
     */
    public ProductSummaryDTO toSummaryDTO(Product product) {
        if (product == null) {
            return null;
        }

        // Calculate status indicators
        boolean needsReorder = product.getQuantityInStock() != null && 
                            product.getReorderPoint() != null && 
                            product.getQuantityInStock() <= product.getReorderPoint();

        String stockStatus = calculateStockStatus(product);

        return ProductSummaryDTO.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .unitPrice(product.getUnitPrice())
                .status(product.getStatus())
                .quantityInStock(product.getQuantityInStock())
                .category(product.getCategory())
                .needsReorder(needsReorder)
                .stockStatus(stockStatus)
                .build();
    }

    /**
     * Convert list of Product entities to ProductResponseDTO list
     */
    public List<ProductResponseDTO> toResponseDTOList(List<Product> products) {
        if (products == null) {
            return null;
        }
        return products.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert list of Product entities to ProductSummaryDTO list
     */
    public List<ProductSummaryDTO> toSummaryDTOList(List<Product> products) {
        if (products == null) {
            return null;
        }
        return products.stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to calculate stock status
     */
    private String calculateStockStatus(Product product) {
        Integer quantity = product.getQuantityInStock();
        Integer reorderPoint = product.getReorderPoint();
        Integer minimumStock = product.getMinimumStockLevel();

        if (quantity == null) {
            return "UNKNOWN";
        }

        if (quantity == 0) {
            return "OUT_OF_STOCK";
        }

        if (reorderPoint != null && quantity <= reorderPoint) {
            return "CRITICAL";
        }

        if (minimumStock != null && quantity < minimumStock) {
            return "LOW";
        }

        return "NORMAL";
    }
}