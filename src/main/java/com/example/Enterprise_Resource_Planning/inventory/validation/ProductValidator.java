package com.example.Enterprise_Resource_Planning.inventory.validation;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.example.Enterprise_Resource_Planning.inventory.exception.InvalidProductDataException;
import com.example.Enterprise_Resource_Planning.inventory.model.Product;
import com.example.Enterprise_Resource_Planning.inventory.model.ProductStatus;
import com.example.Enterprise_Resource_Planning.inventory.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

/**
 * Validator class for Product entities.
 * Provides comprehensive validation logic for product data and business rules.
 */
@Component
@RequiredArgsConstructor
public class ProductValidator {
    
    private final ProductRepository productRepository;
    
    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Z0-9-_]+$");
    private static final BigDecimal MAX_PRICE = new BigDecimal("999999.99");
    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;
    private static final BigDecimal MAX_WEIGHT = new BigDecimal("9999.999");
    private static final BigDecimal MIN_WEIGHT = new BigDecimal("0.001");
    
    /**
     * Validates a product entity for creation or update.
     * 
     * @param product the product to validate
     * @throws InvalidProductDataException if validation fails
     */
    public void validateProduct(Product product) {
        if (product == null) {
            throw new InvalidProductDataException("Product cannot be null");
        }
        
        validateSku(product.getSku());
        validateName(product.getName());
        validateDescription(product.getDescription());
        validateUnitPrice(product.getUnitPrice());
        validateStatus(product.getStatus());
        validateQuantityInStock(product.getQuantityInStock());
        validateCategory(product.getCategory());
        validateSupplier(product.getSupplier());
        validateWeight(product.getWeightKg());
        validateStockLevels(product.getMinimumStockLevel(), product.getReorderPoint());
        
        // Business rule validations
        validateStockBusinessRules(product);
        validatePriceBusinessRules(product);
    }

    /**
     * Validates product SKU.
     * 
     * @param sku the SKU to validate
     * @throws InvalidProductDataException if validation fails
     */
    public void validateSku(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            throw new InvalidProductDataException("Product SKU is required");
        }
        
        String trimmedSku = sku.trim();
        
        if (trimmedSku.length() < 3) {
            throw new InvalidProductDataException("Product SKU must be at least 3 characters long");
        }
        
        if (trimmedSku.length() > 50) {
            throw new InvalidProductDataException("Product SKU cannot exceed 50 characters");
        }
        
        if (!SKU_PATTERN.matcher(trimmedSku).matches()) {
            throw new InvalidProductDataException("Product SKU can only contain uppercase letters, numbers, hyphens, and underscores");
        }
        
        // Check for invalid patterns
        if (trimmedSku.startsWith("-") || trimmedSku.endsWith("-") || 
            trimmedSku.startsWith("_") || trimmedSku.endsWith("_")) {
            throw new InvalidProductDataException("Product SKU cannot start or end with hyphens or underscores");
        }
        
        if (trimmedSku.contains("--") || trimmedSku.contains("__")) {
            throw new InvalidProductDataException("Product SKU cannot contain consecutive hyphens or underscores");
        }
    }

    /**
     * Validates product name.
     * 
     * @param name the name to validate
     * @throws InvalidProductDataException if validation fails
     */
    public void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidProductDataException("Product name is required");
        }
        
        String trimmedName = name.trim();
        
        if (trimmedName.length() < 2) {
            throw new InvalidProductDataException("Product name must be at least 2 characters long");
        }
        
        if (trimmedName.length() > 200) {
            throw new InvalidProductDataException("Product name cannot exceed 200 characters");
        }
        
        // Check for valid characters (letters, numbers, spaces, common punctuation)
        if (!trimmedName.matches("^[a-zA-Z0-9\\s&'.,()/-]+$")) {
            throw new InvalidProductDataException("Product name contains invalid characters");
        }
        
        // Must contain at least one alphanumeric character
        if (!trimmedName.matches(".*[a-zA-Z0-9].*")) {
            throw new InvalidProductDataException("Product name must contain at least one alphanumeric character");
        }
    }

    /**
     * Validates product description.
     * 
     * @param description the description to validate
     * @throws InvalidProductDataException if validation fails
     */
    public void validateDescription(String description) {
        if (description != null) {
            if (description.length() > 1000) {
                throw new InvalidProductDataException("Product description cannot exceed 1000 characters");
            }
            
            if (description.trim().length() > 0 && description.trim().length() < 10) {
                throw new InvalidProductDataException("Product description must be at least 10 characters if provided");
            }
        }
    }

    /**
     * Validates product unit price.
     * 
     * @param unitPrice the unit price to validate
     * @throws InvalidProductDataException if validation fails
     */
    public void validateUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null) {
            throw new InvalidProductDataException("Product unit price is required");
        }
        
        if (unitPrice.compareTo(MIN_PRICE) < 0) {
            throw new InvalidProductDataException("Product unit price cannot be negative");
        }
        
        if (unitPrice.compareTo(MAX_PRICE) > 0) {
            throw new InvalidProductDataException("Product unit price cannot exceed " + MAX_PRICE);
        }
        
        if (unitPrice.scale() > 2) {
            throw new InvalidProductDataException("Product unit price cannot have more than 2 decimal places");
        }
        
        // Business rule: Price should be reasonable (not zero for sellable products)
        if (unitPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidProductDataException("Product unit price should be greater than zero");
        }
    }

    /**
     * Validates product status.
     * 
     * @param status the status to validate
     * @throws InvalidProductDataException if validation fails
     */
    public void validateStatus(ProductStatus status) {
        if (status == null) {
            throw new InvalidProductDataException("Product status is required");
        }
    }

    /**
     * Validates quantity in stock.
     * 
     * @param quantityInStock the quantity to validate
     * @throws InvalidProductDataException if validation fails
     */
    public void validateQuantityInStock(Integer quantityInStock) {
        if (quantityInStock == null) {
            throw new InvalidProductDataException("Product quantity in stock is required");
        }
        
        if (quantityInStock < 0) {
            throw new InvalidProductDataException("Product quantity in stock cannot be negative");
        }
        
        if (quantityInStock > 1000000) {
            throw new InvalidProductDataException("Product quantity in stock cannot exceed 1,000,000");
        }
    }

    /**
     * Validates product category.
     * 
     * @param category the category to validate
     * @throws InvalidProductDataException if validation fails
     */
    public void validateCategory(String category) {
        if (category != null && !category.trim().isEmpty()) {
            String trimmedCategory = category.trim();
            
            if (trimmedCategory.length() > 100) {
                throw new InvalidProductDataException("Product category cannot exceed 100 characters");
            }
            
            if (trimmedCategory.length() < 2) {
                throw new InvalidProductDataException("Product category must be at least 2 characters if provided");
            }
            
            if (!trimmedCategory.matches("^[a-zA-Z0-9\\s&'.,()/-]+$")) {
                throw new InvalidProductDataException("Product category contains invalid characters");
            }
        }
    }

    /**
     * Validates product supplier.
     * 
     * @param supplier the supplier to validate
     * @throws InvalidProductDataException if validation fails
     */
    public void validateSupplier(String supplier) {
        if (supplier != null && !supplier.trim().isEmpty()) {
            String trimmedSupplier = supplier.trim();
            
            if (trimmedSupplier.length() > 150) {
                throw new InvalidProductDataException("Product supplier cannot exceed 150 characters");
            }
            
            if (trimmedSupplier.length() < 2) {
                throw new InvalidProductDataException("Product supplier must be at least 2 characters if provided");
            }
            
            if (!trimmedSupplier.matches("^[a-zA-Z0-9\\s&'.,()/-]+$")) {
                throw new InvalidProductDataException("Product supplier contains invalid characters");
            }
        }
    }

    /**
     * Validates product weight.
     * 
     * @param weight the weight to validate
     * @throws InvalidProductDataException if validation fails
     */
    public void validateWeight(BigDecimal weight) {
        if (weight != null) {
            if (weight.compareTo(MIN_WEIGHT) < 0) {
                throw new InvalidProductDataException("Product weight must be at least " + MIN_WEIGHT + " kg");
            }
            
            if (weight.compareTo(MAX_WEIGHT) > 0) {
                throw new InvalidProductDataException("Product weight cannot exceed " + MAX_WEIGHT + " kg");
            }
            
            if (weight.scale() > 3) {
                throw new InvalidProductDataException("Product weight cannot have more than 3 decimal places");
            }
        }
    }

    /**
     * Validates stock levels.
     * 
     * @param minimumStockLevel the minimum stock level
     * @param reorderPoint the reorder point
     * @throws InvalidProductDataException if validation fails
     */
    public void validateStockLevels(Integer minimumStockLevel, Integer reorderPoint) {
        if (minimumStockLevel != null && minimumStockLevel < 0) {
            throw new InvalidProductDataException("Minimum stock level cannot be negative");
        }
        
        if (reorderPoint != null && reorderPoint < 0) {
            throw new InvalidProductDataException("Reorder point cannot be negative");
        }
        
        if (minimumStockLevel != null && reorderPoint != null) {
            if (reorderPoint > minimumStockLevel) {
                throw new InvalidProductDataException("Reorder point should not exceed minimum stock level");
            }
        }
        
        if (minimumStockLevel != null && minimumStockLevel > 100000) {
            throw new InvalidProductDataException("Minimum stock level cannot exceed 100,000");
        }
        
        if (reorderPoint != null && reorderPoint > 100000) {
            throw new InvalidProductDataException("Reorder point cannot exceed 100,000");
        }
    }

    /**
     * Validates product ID for update and delete operations.
     * 
     * @param id the product ID to validate
     * @throws InvalidProductDataException if validation fails
     */
    public void validateProductId(Long id) {
        if (id == null) {
            throw new InvalidProductDataException("Product ID cannot be null");
        }
        
        if (id <= 0) {
            throw new InvalidProductDataException("Product ID must be a positive number");
        }
    }

    /**
     * Validates product IDs for bulk operations.
     * 
     * @param ids the product IDs to validate
     * @throws InvalidProductDataException if validation fails
     */
    public void validateProductIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new InvalidProductDataException("Product IDs list cannot be null or empty");
        }
        
        if (ids.size() > 1000) {
            throw new InvalidProductDataException("Cannot process more than 1000 products at once");
        }
        
        ids.forEach(this::validateProductId);
        
        // Check for duplicates
        long distinctCount = ids.stream().distinct().count();
        if (distinctCount != ids.size()) {
            throw new InvalidProductDataException("Product IDs list contains duplicates");
        }
    }

    /**
     * Validates SKU uniqueness for creation.
     * 
     * @param sku the SKU to check
     * @throws InvalidProductDataException if SKU already exists
     */
    public void validateSkuUniqueness(String sku) {
        validateSku(sku);
        
        if (productRepository.existsBySku(sku)) {
            throw new InvalidProductDataException("Product with SKU '" + sku + "' already exists");
        }
    }

    /**
     * Validates SKU uniqueness for update.
     * 
     * @param sku the SKU to check
     * @param productId the ID of the product being updated
     * @throws InvalidProductDataException if SKU already exists for another product
     */
    public void validateSkuUniquenessForUpdate(String sku, Long productId) {
        validateSku(sku);
        validateProductId(productId);
        
        if (productRepository.existsBySkuAndIdNot(sku, productId)) {
            throw new InvalidProductDataException("Product with SKU '" + sku + "' already exists");
        }
    }

    /**
     * Validates stock-related business rules.
     * 
     * @param product the product to validate
     * @throws InvalidProductDataException if validation fails
     */
    private void validateStockBusinessRules(Product product) {
        // Check if out of stock status matches actual quantity
        if (product.getStatus() == ProductStatus.OUT_OF_STOCK && 
            product.getQuantityInStock() != null && product.getQuantityInStock() > 0) {
            throw new InvalidProductDataException("Product status is OUT_OF_STOCK but quantity in stock is greater than zero");
        }
        
        // Check if available status is valid when no stock
        if (product.getStatus() == ProductStatus.AVAILABLE && 
            product.getQuantityInStock() != null && product.getQuantityInStock() == 0) {
            throw new InvalidProductDataException("Product status is AVAILABLE but quantity in stock is zero");
        }
    }

    /**
     * Validates price-related business rules.
     * 
     * @param product the product to validate
     * @throws InvalidProductDataException if validation fails
     */
    private void validatePriceBusinessRules(Product product) {
        // Discontinued products shouldn't have high prices
        if (product.getStatus() == ProductStatus.DISCONTINUED && 
            product.getUnitPrice() != null && product.getUnitPrice().compareTo(new BigDecimal("10000")) > 0) {
            throw new InvalidProductDataException("Discontinued products should not have unit price exceeding $10,000");
        }
        
        // Pre-order products should have reasonable prices
        if (product.getStatus() == ProductStatus.PRE_ORDER && 
            product.getUnitPrice() != null && product.getUnitPrice().compareTo(new BigDecimal("0.01")) < 0) {
            throw new InvalidProductDataException("Pre-order products must have unit price of at least $0.01");
        }
    }

    /**
     * Validates product for creation (no ID should be present).
     * 
     * @param product the product to validate for creation
     * @throws InvalidProductDataException if validation fails
     */
    public void validateForCreation(Product product) {
        validateProduct(product);
        
        if (product.getId() != null) {
            throw new InvalidProductDataException("Product ID should not be provided for creation");
        }
        
        validateSkuUniqueness(product.getSku());
    }

    /**
     * Validates product for update (ID must be present).
     * 
     * @param product the product to validate for update
     * @throws InvalidProductDataException if validation fails
     */
    public void validateForUpdate(Product product) {
        validateProduct(product);
        validateProductId(product.getId());
        validateSkuUniquenessForUpdate(product.getSku(), product.getId());
    }

    /**
     * Validates product for deletion.
     * 
     * @param productId the product ID to validate for deletion
     * @throws InvalidProductDataException if validation fails
     */
    public void validateForDeletion(Long productId) {
        validateProductId(productId);
    }

    
    /**
     * Validates ProductCreateDTO for creation.
     * 
     * @param productCreateDTO the DTO to validate for creation
     * @throws InvalidProductDataException if validation fails
     */
    public void validateProductForCreation(com.example.Enterprise_Resource_Planning.inventory.dto.request.ProductCreateDTO productCreateDTO) {
        if (productCreateDTO == null) {
            throw new InvalidProductDataException("Product creation data cannot be null");
        }
        
        // Validate individual fields using existing methods
        validateSku(productCreateDTO.getSku());
        validateName(productCreateDTO.getName());
        validateDescription(productCreateDTO.getDescription());
        validateUnitPrice(productCreateDTO.getUnitPrice());
        validateQuantityInStock(productCreateDTO.getQuantityInStock());
        validateCategory(productCreateDTO.getCategory());
        validateSupplier(productCreateDTO.getSupplier());
        validateWeight(productCreateDTO.getWeightKg());
        validateStockLevels(productCreateDTO.getMinimumStockLevel(), productCreateDTO.getReorderPoint());
        
        // Check SKU uniqueness
        validateSkuUniqueness(productCreateDTO.getSku());
        
        // Validate stock level relationships
        if (productCreateDTO.getMinimumStockLevel() != null && 
            productCreateDTO.getReorderPoint() != null &&
            productCreateDTO.getReorderPoint() > productCreateDTO.getMinimumStockLevel()) {
            throw new InvalidProductDataException("Reorder point should not exceed minimum stock level");
        }
    }

    /**
     * Validates ProductUpdateDTO for update operations.
     * 
     * @param productUpdateDTO the DTO to validate for update
     * @param productId the ID of the product being updated
     * @throws InvalidProductDataException if validation fails
     */
    public void validateProductForUpdate(com.example.Enterprise_Resource_Planning.inventory.dto.request.ProductUpdateDTO productUpdateDTO, Long productId) {
        if (productUpdateDTO == null) {
            throw new InvalidProductDataException("Product update data cannot be null");
        }
        
        validateProductId(productId);
        
        // Validate only provided fields (since it's a partial update)
        if (productUpdateDTO.getSku() != null) {
            validateSkuUniquenessForUpdate(productUpdateDTO.getSku(), productId);
        }
        
        if (productUpdateDTO.getName() != null) {
            validateName(productUpdateDTO.getName());
        }
        
        if (productUpdateDTO.getDescription() != null) {
            validateDescription(productUpdateDTO.getDescription());
        }
        
        if (productUpdateDTO.getUnitPrice() != null) {
            validateUnitPrice(productUpdateDTO.getUnitPrice());
        }
        
        if (productUpdateDTO.getStatus() != null) {
            validateStatus(productUpdateDTO.getStatus());
        }
        
        if (productUpdateDTO.getQuantityInStock() != null) {
            validateQuantityInStock(productUpdateDTO.getQuantityInStock());
        }
        
        if (productUpdateDTO.getCategory() != null) {
            validateCategory(productUpdateDTO.getCategory());
        }
        
        if (productUpdateDTO.getSupplier() != null) {
            validateSupplier(productUpdateDTO.getSupplier());
        }
        
        if (productUpdateDTO.getWeightKg() != null) {
            validateWeight(productUpdateDTO.getWeightKg());
        }
        
        if (productUpdateDTO.getMinimumStockLevel() != null || productUpdateDTO.getReorderPoint() != null) {
            validateStockLevels(productUpdateDTO.getMinimumStockLevel(), productUpdateDTO.getReorderPoint());
        }
    }
}
