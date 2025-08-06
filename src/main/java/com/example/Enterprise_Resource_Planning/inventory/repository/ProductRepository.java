package com.example.Enterprise_Resource_Planning.inventory.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Enterprise_Resource_Planning.inventory.model.Product;
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
}
