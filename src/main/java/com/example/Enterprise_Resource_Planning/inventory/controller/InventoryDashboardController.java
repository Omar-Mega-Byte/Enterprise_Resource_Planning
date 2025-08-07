package com.example.Enterprise_Resource_Planning.inventory.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Enterprise_Resource_Planning.inventory.dto.response.ProductSummaryDTO;
import com.example.Enterprise_Resource_Planning.inventory.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for Inventory Dashboard operations.
 * Provides summary statistics and dashboard data for inventory management.
 */
@RestController
@RequestMapping("/inventory/dashboard")
@RequiredArgsConstructor
@Tag(name = "Inventory Dashboard", description = "APIs for inventory dashboard and statistics")
public class InventoryDashboardController {

    private final ProductService productService;

    /**
     * Get inventory dashboard summary.
     */
    @GetMapping("/summary")
    @Operation(summary = "Get inventory summary", description = "Get overall inventory statistics and metrics")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Summary retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getInventorySummary() {
        List<ProductSummaryDTO> allProducts = productService.getAllProducts();
        List<ProductSummaryDTO> lowStockProducts = productService.getProductsBelowMinimumStock();
        List<ProductSummaryDTO> reorderProducts = productService.getProductsNeedingReorder();
        
        Map<String, Object> summary = new HashMap<>();
        
        // Basic counts
        summary.put("totalProducts", allProducts.size());
        summary.put("lowStockCount", lowStockProducts.size());
        summary.put("reorderAlertsCount", reorderProducts.size());
        
        // Calculate total inventory value
        BigDecimal totalValue = allProducts.stream()
            .map(product -> {
                BigDecimal price = product.getUnitPrice() != null ? product.getUnitPrice() : BigDecimal.ZERO;
                Integer quantity = Objects.requireNonNullElse(product.getQuantityInStock(), 0);
                return price.multiply(BigDecimal.valueOf(quantity));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        summary.put("totalInventoryValue", totalValue);
        
        // Calculate average product value
        BigDecimal averageValue = allProducts.isEmpty() ? BigDecimal.ZERO : 
            totalValue.divide(BigDecimal.valueOf(allProducts.size()), 2, RoundingMode.HALF_UP);
        summary.put("averageProductValue", averageValue);
        
        // Status distribution
        Map<String, Long> statusDistribution = allProducts.stream()
            .collect(Collectors.groupingBy(
                product -> product.getStockStatus() != null ? product.getStockStatus() : "UNKNOWN",
                Collectors.counting()
            ));
        summary.put("stockStatusDistribution", statusDistribution);
        
        return ResponseEntity.ok(summary);
    }

    /**
     * Get low stock alerts.
     */
    @GetMapping("/alerts/low-stock")
    @Operation(summary = "Get low stock alerts", description = "Get products that are below minimum stock level")
    public ResponseEntity<List<ProductSummaryDTO>> getLowStockAlerts() {
        List<ProductSummaryDTO> products = productService.getProductsBelowMinimumStock();
        return ResponseEntity.ok(products);
    }

    /**
     * Get reorder alerts.
     */
    @GetMapping("/alerts/reorder")
    @Operation(summary = "Get reorder alerts", description = "Get products that need to be reordered")
    public ResponseEntity<List<ProductSummaryDTO>> getReorderAlerts() {
        List<ProductSummaryDTO> products = productService.getProductsNeedingReorder();
        return ResponseEntity.ok(products);
    }

    /**
     * Get inventory metrics by category.
     */
    @GetMapping("/metrics/by-category")
    @Operation(summary = "Get metrics by category", description = "Get inventory metrics grouped by product category")
    public ResponseEntity<Map<String, Object>> getMetricsByCategory() {
        List<ProductSummaryDTO> allProducts = productService.getAllProducts();
        
        Map<String, Map<String, Object>> categoryMetrics = new HashMap<>();
        
        // Group products by category
        Map<String, List<ProductSummaryDTO>> productsByCategory = allProducts.stream()
            .collect(Collectors.groupingBy(
                product -> product.getCategory() != null ? product.getCategory() : "Uncategorized"
            ));
        
        // Calculate metrics for each category
        productsByCategory.forEach((category, products) -> {
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("productCount", products.size());
            
            BigDecimal totalValue = products.stream()
                .map(product -> {
                    BigDecimal price = product.getUnitPrice() != null ? product.getUnitPrice() : BigDecimal.ZERO;
                    Integer quantity = Objects.requireNonNullElse(product.getQuantityInStock(), 0);
                    return price.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            metrics.put("totalValue", totalValue);
            
            long lowStockCount = products.stream()
                .filter(product -> "LOW".equals(product.getStockStatus()) || "OUT_OF_STOCK".equals(product.getStockStatus()))
                .count();
            metrics.put("lowStockCount", lowStockCount);
            
            categoryMetrics.put(category, metrics);
        });
        
        Map<String, Object> response = new HashMap<>();
        response.put("categoryMetrics", categoryMetrics);
        response.put("totalCategories", categoryMetrics.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get top valuable products.
     */
    @GetMapping("/top-valuable")
    @Operation(summary = "Get top valuable products", description = "Get products with highest inventory value")
    public ResponseEntity<List<ProductSummaryDTO>> getTopValuableProducts() {
        List<ProductSummaryDTO> allProducts = productService.getAllProducts();
        
        // Sort by inventory value (price * quantity) in descending order
        List<ProductSummaryDTO> topProducts = allProducts.stream()
            .sorted((p1, p2) -> {
                BigDecimal value1 = calculateInventoryValue(p1);
                BigDecimal value2 = calculateInventoryValue(p2);
                return value2.compareTo(value1); // Descending order
            })
            .limit(10) // Top 10
            .toList();
        
        return ResponseEntity.ok(topProducts);
    }

    /**
     * Helper method to calculate inventory value for a product.
     */
    private BigDecimal calculateInventoryValue(ProductSummaryDTO product) {
        BigDecimal price = product.getUnitPrice() != null ? product.getUnitPrice() : BigDecimal.ZERO;
        Integer quantity = Objects.requireNonNullElse(product.getQuantityInStock(), 0);
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
