package com.example.Enterprise_Resource_Planning.inventory.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Enterprise_Resource_Planning.inventory.dto.request.ProductCreateDTO;
import com.example.Enterprise_Resource_Planning.inventory.dto.request.ProductSearchCriteria;
import com.example.Enterprise_Resource_Planning.inventory.dto.request.ProductUpdateDTO;
import com.example.Enterprise_Resource_Planning.inventory.dto.response.ProductResponseDTO;
import com.example.Enterprise_Resource_Planning.inventory.dto.response.ProductSummaryDTO;
import com.example.Enterprise_Resource_Planning.inventory.model.ProductStatus;
import com.example.Enterprise_Resource_Planning.inventory.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for Product management operations.
 * Provides comprehensive CRUD operations and advanced search functionality.
 */
@RestController
@RequestMapping("/inventory/products")
@RequiredArgsConstructor
@Validated
@Tag(name = "Product Management", description = "APIs for managing inventory products")
public class ProductController {

    private final ProductService productService;

    // ===================================
    // CRUD OPERATIONS
    // ===================================

    /**
     * Create a new product.
     */
    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product in the inventory")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid product data"),
        @ApiResponse(responseCode = "409", description = "Product with SKU already exists")
    })
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductCreateDTO productCreateDTO) {
        ProductResponseDTO createdProduct = productService.createProduct(productCreateDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Get product by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves a product by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponseDTO> getProductById(
            @Parameter(description = "Product ID") @PathVariable @Min(1) Long id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Get product by SKU.
     */
    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get product by SKU", description = "Retrieves a product by its SKU")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponseDTO> getProductBySku(
            @Parameter(description = "Product SKU") @PathVariable @NotBlank String sku) {
        ProductResponseDTO product = productService.getProductBySku(sku);
        return ResponseEntity.ok(product);
    }

    /**
     * Update an existing product.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Updates an existing product")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(description = "Product ID") @PathVariable @Min(1) Long id,
            @Valid @RequestBody ProductUpdateDTO productUpdateDTO) {
        ProductResponseDTO updatedProduct = productService.updateProduct(id, productUpdateDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Delete a product.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Deletes a product from the inventory")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable @Min(1) Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // ===================================
    // SEARCH AND LISTING OPERATIONS
    // ===================================

    /**
     * Get all products (summary view).
     */
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves all products in summary format")
    public ResponseEntity<List<ProductSummaryDTO>> getAllProducts() {
        List<ProductSummaryDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Search products with criteria.
     */
    @PostMapping("/search")
    @Operation(summary = "Search products", description = "Search products with multiple criteria")
    public ResponseEntity<List<ProductSummaryDTO>> searchProducts(
            @RequestBody ProductSearchCriteria criteria) {
        List<ProductSummaryDTO> products = productService.searchProducts(criteria);
        return ResponseEntity.ok(products);
    }

    /**
     * Search products with pagination.
     */
    @PostMapping("/search/pageable")
    @Operation(summary = "Search products with pagination", description = "Search products with pagination support")
    public ResponseEntity<Page<ProductSummaryDTO>> searchProductsPageable(
            @RequestBody ProductSearchCriteria criteria) {
        Page<ProductSummaryDTO> products = productService.searchProductsPageable(criteria);
        return ResponseEntity.ok(products);
    }

    /**
     * Search products by name.
     */
    @GetMapping("/search/name")
    @Operation(summary = "Search by name", description = "Search products by name (partial match)")
    public ResponseEntity<List<ProductSummaryDTO>> searchByName(
            @Parameter(description = "Product name to search") @RequestParam @NotBlank String name) {
        List<ProductSummaryDTO> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    /**
     * Search products by category.
     */
    @GetMapping("/search/category")
    @Operation(summary = "Search by category", description = "Search products by category")
    public ResponseEntity<List<ProductSummaryDTO>> searchByCategory(
            @Parameter(description = "Product category") @RequestParam @NotBlank String category) {
        List<ProductSummaryDTO> products = productService.searchProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    /**
     * Search products by supplier.
     */
    @GetMapping("/search/supplier")
    @Operation(summary = "Search by supplier", description = "Search products by supplier")
    public ResponseEntity<List<ProductSummaryDTO>> searchBySupplier(
            @Parameter(description = "Product supplier") @RequestParam @NotBlank String supplier) {
        List<ProductSummaryDTO> products = productService.searchProductsBySupplier(supplier);
        return ResponseEntity.ok(products);
    }

    /**
     * Search products by status.
     */
    @GetMapping("/search/status")
    @Operation(summary = "Search by status", description = "Search products by status")
    public ResponseEntity<List<ProductSummaryDTO>> searchByStatus(
            @Parameter(description = "Product status") @RequestParam ProductStatus status) {
        List<ProductSummaryDTO> products = productService.searchProductsByStatus(status);
        return ResponseEntity.ok(products);
    }

    // ===================================
    // INVENTORY MANAGEMENT OPERATIONS
    // ===================================

    /**
     * Update product stock quantity.
     */
    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update stock", description = "Updates the stock quantity of a product")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid quantity")
    })
    public ResponseEntity<ProductResponseDTO> updateStock(
            @Parameter(description = "Product ID") @PathVariable @Min(1) Long id,
            @Parameter(description = "New stock quantity") @RequestParam @Min(0) Integer quantity) {
        ProductResponseDTO updatedProduct = productService.updateProductStock(id, quantity);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Get products needing reorder.
     */
    @GetMapping("/reorder-alerts")
    @Operation(summary = "Get reorder alerts", description = "Get products that need reordering")
    public ResponseEntity<List<ProductSummaryDTO>> getProductsNeedingReorder() {
        List<ProductSummaryDTO> products = productService.getProductsNeedingReorder();
        return ResponseEntity.ok(products);
    }

    /**
     * Get products below minimum stock.
     */
    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products", description = "Get products below minimum stock level")
    public ResponseEntity<List<ProductSummaryDTO>> getProductsBelowMinimumStock() {
        List<ProductSummaryDTO> products = productService.getProductsBelowMinimumStock();
        return ResponseEntity.ok(products);
    }

    // ===================================
    // UTILITY OPERATIONS
    // ===================================

    /**
     * Check if SKU exists.
     */
    @GetMapping("/check-sku")
    @Operation(summary = "Check SKU availability", description = "Check if a SKU is available")
    public ResponseEntity<Boolean> checkSkuExists(
            @Parameter(description = "SKU to check") @RequestParam @NotBlank String sku) {
        boolean exists = productService.existsBySku(sku);
        return ResponseEntity.ok(exists);
    }
}
