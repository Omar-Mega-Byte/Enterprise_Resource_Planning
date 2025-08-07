package com.example.Enterprise_Resource_Planning.inventory.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Enterprise_Resource_Planning.inventory.model.Product;
import com.example.Enterprise_Resource_Planning.inventory.model.ProductStatus;
/**
 * Repository interface for Product entities.
 * Provides methods to perform CRUD operations and custom queries.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds a product by its name.
     * 
     * @param name the name of the product
     * @return the product if found, or null if not found
     */
    Optional<Product> findByName(String name);

    /**
     * Checks if a product with the given name exists.
     * 
     * @param name the name of the product
     * @return true if a product with the given name exists, false otherwise
     */
    boolean existsByName(String name);
    
    /**
     * Find a product by its SKU.
     * 
     * @param sku the SKU to search for
     * @return Optional containing the product if found
     */
    Optional<Product> findBySku(String sku);
    
    /**
     * Check if a product exists with the given SKU.
     * 
     * @param sku the SKU to check
     * @return true if a product exists with the SKU
     */
    boolean existsBySku(String sku);
    
    /**
     * Check if a product exists with the given SKU excluding a specific product ID.
     * Used for validating SKU uniqueness during updates.
     * 
     * @param sku the SKU to check
     * @param id the product ID to exclude from the check
     * @return true if another product exists with the same SKU
     */
    boolean existsBySkuAndIdNot(String sku, Long id);
    
    //         ===================================
    //                  SEARCH METHODS
    //         ===================================
    
    /**
     * Find products by name containing a substring (case-insensitive).
     */
    List<Product> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find products by SKU containing a substring (case-insensitive).
     */
    List<Product> findBySkuContainingIgnoreCase(String sku);
    
    /**
     * Find products by description containing a substring (case-insensitive).
     */
    List<Product> findByDescriptionContainingIgnoreCase(String description);
    
    /**
     * Find products by status.
     */
    List<Product> findByStatus(ProductStatus status);
    
    /**
     * Find products by category (case-insensitive).
     */
    List<Product> findByCategoryIgnoreCase(String category);
    
    /**
     * Find products by supplier (case-insensitive).
     */
    List<Product> findBySupplierIgnoreCase(String supplier);
    
    /**
     * Find products within a price range.
     */
    List<Product> findByUnitPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Find products with quantity between min and max.
     */
    List<Product> findByQuantityInStockBetween(Integer minQuantity, Integer maxQuantity);
    
    /**
     * Find products with weight between min and max.
     */
    List<Product> findByWeightKgBetween(BigDecimal minWeight, BigDecimal maxWeight);
    
    /**
     * Find products that need reordering (quantity <= reorder point).
     */
    @Query("SELECT p FROM Product p WHERE p.quantityInStock <= p.reorderPoint")
    List<Product> findProductsNeedingReorder();
    
    /**
     * Find products below minimum stock level.
     */
    @Query("SELECT p FROM Product p WHERE p.quantityInStock < p.minimumStockLevel")
    List<Product> findProductsBelowMinimumStock();
    
    /**
     * Find products with stock quantity less than or equal to specified value.
     */
    List<Product> findByQuantityInStockLessThanEqual(Integer quantity);
    
    /**
     * Complex search query with multiple optional criteria.
     */
    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:sku IS NULL OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :sku, '%'))) AND " +
            "(:description IS NULL OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
            "(:status IS NULL OR p.status = :status) AND " +
            "(:category IS NULL OR LOWER(p.category) = LOWER(:category)) AND " +
            "(:supplier IS NULL OR LOWER(p.supplier) = LOWER(:supplier)) AND " +
            "(:minPrice IS NULL OR p.unitPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.unitPrice <= :maxPrice) AND " +
            "(:minQuantity IS NULL OR p.quantityInStock >= :minQuantity) AND " +
            "(:maxQuantity IS NULL OR p.quantityInStock <= :maxQuantity) AND " +
            "(:minWeight IS NULL OR p.weightKg >= :minWeight) AND " +
            "(:maxWeight IS NULL OR p.weightKg <= :maxWeight)")
    List<Product> findProductsByCriteria(
        @Param("name") String name,
        @Param("sku") String sku,
        @Param("description") String description,
        @Param("status") ProductStatus status,
        @Param("category") String category,
        @Param("supplier") String supplier,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("minQuantity") Integer minQuantity,
        @Param("maxQuantity") Integer maxQuantity,
        @Param("minWeight") BigDecimal minWeight,
        @Param("maxWeight") BigDecimal maxWeight
    );
    
    /**
     * Paginated version of complex search.
     */
    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:sku IS NULL OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :sku, '%'))) AND " +
            "(:description IS NULL OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
            "(:status IS NULL OR p.status = :status) AND " +
            "(:category IS NULL OR LOWER(p.category) = LOWER(:category)) AND " +
            "(:supplier IS NULL OR LOWER(p.supplier) = LOWER(:supplier)) AND " +
            "(:minPrice IS NULL OR p.unitPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.unitPrice <= :maxPrice) AND " +
            "(:minQuantity IS NULL OR p.quantityInStock >= :minQuantity) AND " +
            "(:maxQuantity IS NULL OR p.quantityInStock <= :maxQuantity) AND " +
            "(:minWeight IS NULL OR p.weightKg >= :minWeight) AND " +
            "(:maxWeight IS NULL OR p.weightKg <= :maxWeight)")
    Page<Product> findProductsByCriteriaPageable(
        @Param("name") String name,
        @Param("sku") String sku,
        @Param("description") String description,
        @Param("status") ProductStatus status,
        @Param("category") String category,
        @Param("supplier") String supplier,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("minQuantity") Integer minQuantity,
        @Param("maxQuantity") Integer maxQuantity,
        @Param("minWeight") BigDecimal minWeight,
        @Param("maxWeight") BigDecimal maxWeight,
        Pageable pageable
    );
}
