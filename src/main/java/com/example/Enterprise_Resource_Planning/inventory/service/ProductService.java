package com.example.Enterprise_Resource_Planning.inventory.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Enterprise_Resource_Planning.inventory.dto.request.ProductCreateDTO;
import com.example.Enterprise_Resource_Planning.inventory.dto.request.ProductSearchCriteria;
import com.example.Enterprise_Resource_Planning.inventory.dto.request.ProductUpdateDTO;
import com.example.Enterprise_Resource_Planning.inventory.dto.response.ProductResponseDTO;
import com.example.Enterprise_Resource_Planning.inventory.dto.response.ProductSummaryDTO;
import com.example.Enterprise_Resource_Planning.inventory.exception.ProductNotFoundException;
import com.example.Enterprise_Resource_Planning.inventory.mapper.ProductMapper;
import com.example.Enterprise_Resource_Planning.inventory.model.Product;
import com.example.Enterprise_Resource_Planning.inventory.model.ProductStatus;
import com.example.Enterprise_Resource_Planning.inventory.repository.ProductRepository;
import com.example.Enterprise_Resource_Planning.inventory.validation.ProductValidator;
@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductValidator productValidator, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
        this.productMapper = productMapper;
    }
    /**
     * Creates a new product.
     * @param productCreateDTO the product data transfer object containing product details
     * @return the created product as a response DTO
     * @throws InvalidProductDataException if product validation fails
     */
    public ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO) {
        productValidator.validateProductForCreation(productCreateDTO);
        Product product = productMapper.toEntity(productCreateDTO);
        product.setStatus(ProductStatus.ACTIVE); // Default status
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }

    /**
     * Updates an existing product.
     * @param productId the ID of the product to update
     * @param productUpdateDTO the product update data transfer object
     * @return the updated product as a response DTO
     * @throws ProductNotFoundException if product is not found
     * @throws InvalidProductDataException if product validation fails
     */
    public ProductResponseDTO updateProduct(Long productId, ProductUpdateDTO productUpdateDTO) {
        productValidator.validateProductForUpdate(productUpdateDTO, productId);
        
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        
        productMapper.updateEntity(existingProduct, productUpdateDTO);
        Product savedProduct = productRepository.save(existingProduct);
        return productMapper.toResponseDTO(savedProduct);
    }

    /**
     * Retrieves a product by its ID.
     * @param productId the ID of the product to retrieve
     * @return the product as a response DTO
     * @throws ProductNotFoundException if product is not found
     */
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long productId) {
        productValidator.validateProductId(productId);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        
        return productMapper.toResponseDTO(product);
    }

    /**
     * Retrieves a product by its SKU.
     * @param sku the SKU of the product to retrieve
     * @return the product as a response DTO
     * @throws ProductNotFoundException if product is not found
     */
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductBySku(String sku) {
        productValidator.validateSku(sku);
        
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with SKU: " + sku));
        
        return productMapper.toResponseDTO(product);
    }

    /**
     * Retrieves all products as summary DTOs.
     * @return list of product summary DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductSummaryDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toSummaryDTOList(products);
    }

    /**
     * Searches products based on criteria.
     * @param criteria the search criteria
     * @return list of product summary DTOs matching the criteria
     */
    @Transactional(readOnly = true)
    public List<ProductSummaryDTO> searchProducts(ProductSearchCriteria criteria) {
        if (criteria == null || !criteria.hasSearchCriteria()) {
            // If no criteria provided, return all products
            return getAllProducts();
        }
        
        // Validate criteria ranges
        validateSearchCriteria(criteria);
        
        List<Product> products;
        
        // Check for special search cases first
        if (Boolean.TRUE.equals(criteria.getNeedsReorder())) {
            products = productRepository.findProductsNeedingReorder();
        } else if (Boolean.TRUE.equals(criteria.getBelowMinimumStock())) {
            products = productRepository.findProductsBelowMinimumStock();
        } else {
            // Use the comprehensive search query
            products = productRepository.findProductsByCriteria(
                criteria.getNameContaining(),
                criteria.getSku(),
                criteria.getDescriptionContaining(),
                criteria.getStatus(),
                criteria.getCategory(),
                criteria.getSupplier(),
                criteria.getMinPrice(),
                criteria.getMaxPrice(),
                criteria.getMinQuantity(),
                criteria.getMaxQuantity(),
                criteria.getMinWeight(),
                criteria.getMaxWeight()
            );
        }
        
        // Apply sorting if specified
        products = applySorting(products, criteria);
        
        // Apply pagination if specified
        products = applyPagination(products, criteria);
        
        return productMapper.toSummaryDTOList(products);
    }

    /**
     * Searches products with pagination support.
     * @param criteria the search criteria
     * @return paginated list of product summary DTOs
     */
    @Transactional(readOnly = true)
    public Page<ProductSummaryDTO> searchProductsPageable(ProductSearchCriteria criteria) {
        if (criteria == null) {
            criteria = new ProductSearchCriteria();
        }
        
        validateSearchCriteria(criteria);
        
        // Create pageable object
        Pageable pageable = createPageable(criteria);
        
        Page<Product> productPage = productRepository.findProductsByCriteriaPageable(
            criteria.getNameContaining(),
            criteria.getSku(),
            criteria.getDescriptionContaining(),
            criteria.getStatus(),
            criteria.getCategory(),
            criteria.getSupplier(),
            criteria.getMinPrice(),
            criteria.getMaxPrice(),
            criteria.getMinQuantity(),
            criteria.getMaxQuantity(),
            criteria.getMinWeight(),
            criteria.getMaxWeight(),
            pageable
        );
        
        return productPage.map(productMapper::toSummaryDTO);
    }

    /**
     * Find products that need reordering.
     * @return list of products that need reordering
     */
    @Transactional(readOnly = true)
    public List<ProductSummaryDTO> getProductsNeedingReorder() {
        List<Product> products = productRepository.findProductsNeedingReorder();
        return productMapper.toSummaryDTOList(products);
    }

    /**
     * Find products below minimum stock level.
     * @return list of products below minimum stock
     */
    @Transactional(readOnly = true)
    public List<ProductSummaryDTO> getProductsBelowMinimumStock() {
        List<Product> products = productRepository.findProductsBelowMinimumStock();
        return productMapper.toSummaryDTOList(products);
    }

    /**
     * Search products by name.
     * @param name the name to search for (partial match)
     * @return list of matching products
     */
    @Transactional(readOnly = true)
    public List<ProductSummaryDTO> searchProductsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Search name cannot be null or empty");
        }
        
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name.trim());
        return productMapper.toSummaryDTOList(products);
    }

    /**
     * Search products by category.
     * @param category the category to search for
     * @return list of products in the category
     */
    @Transactional(readOnly = true)
    public List<ProductSummaryDTO> searchProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        
        List<Product> products = productRepository.findByCategoryIgnoreCase(category.trim());
        return productMapper.toSummaryDTOList(products);
    }

    /**
     * Search products by supplier.
     * @param supplier the supplier to search for
     * @return list of products from the supplier
     */
    @Transactional(readOnly = true)
    public List<ProductSummaryDTO> searchProductsBySupplier(String supplier) {
        if (supplier == null || supplier.trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier cannot be null or empty");
        }
        
        List<Product> products = productRepository.findBySupplierIgnoreCase(supplier.trim());
        return productMapper.toSummaryDTOList(products);
    }

    /**
     * Search products by status.
     * @param status the product status
     * @return list of products with the status
     */
    @Transactional(readOnly = true)
    public List<ProductSummaryDTO> searchProductsByStatus(ProductStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        
        List<Product> products = productRepository.findByStatus(status);
        return productMapper.toSummaryDTOList(products);
    }

    // ===================================
    // PRIVATE HELPER METHODS
    // ===================================

    /**
     * Validates search criteria.
     */
    private void validateSearchCriteria(ProductSearchCriteria criteria) {
        if (criteria == null) {
            return;
        }
        
        if (!criteria.isPriceRangeValid()) {
            throw new IllegalArgumentException("Invalid price range: minimum price cannot be greater than maximum price");
        }
        
        if (!criteria.isQuantityRangeValid()) {
            throw new IllegalArgumentException("Invalid quantity range: minimum quantity cannot be greater than maximum quantity");
        }
        
        if (!criteria.isWeightRangeValid()) {
            throw new IllegalArgumentException("Invalid weight range: minimum weight cannot be greater than maximum weight");
        }
    }

    /**
     * Applies sorting to the product list.
     */
    private List<Product> applySorting(List<Product> products, ProductSearchCriteria criteria) {
        if (criteria.getSortBy() == null || criteria.getSortBy().trim().isEmpty()) {
            return products;
        }
        
        String sortBy = criteria.getSortBy().toLowerCase();
        boolean ascending = !"desc".equalsIgnoreCase(criteria.getSortDirection());
        
        return products.stream()
                .sorted((p1, p2) -> {
                    int result = 0;
                    switch (sortBy) {
                        case "name":
                            result = p1.getName().compareToIgnoreCase(p2.getName());
                            break;
                        case "sku":
                            result = p1.getSku().compareToIgnoreCase(p2.getSku());
                            break;
                        case "unitprice":
                            result = p1.getUnitPrice().compareTo(p2.getUnitPrice());
                            break;
                        case "quantityinstock":
                            result = p1.getQuantityInStock().compareTo(p2.getQuantityInStock());
                            break;
                        case "createdat":
                            result = p1.getCreatedAt().compareTo(p2.getCreatedAt());
                            break;
                        default:
                            result = p1.getName().compareToIgnoreCase(p2.getName());
                    }
                    return ascending ? result : -result;
                })
                .toList();
    }

    /**
     * Applies pagination to the product list.
     */
    private List<Product> applyPagination(List<Product> products, ProductSearchCriteria criteria) {
        if (criteria.getPage() == null || criteria.getSize() == null) {
            return products;
        }
        
        int page = Math.max(0, criteria.getPage());
        int size = Math.max(1, Math.min(100, criteria.getSize())); // Max 100 items per page
        int startIndex = page * size;
        
        if (startIndex >= products.size()) {
            return List.of(); // Empty list if page is beyond available data
        }
        
        int endIndex = Math.min(startIndex + size, products.size());
        return products.subList(startIndex, endIndex);
    }

    /**
     * Creates a Pageable object from search criteria.
     */
    private Pageable createPageable(ProductSearchCriteria criteria) {
        int page = criteria.getPage() != null ? Math.max(0, criteria.getPage()) : 0;
        int size = criteria.getSize() != null ? Math.max(1, Math.min(100, criteria.getSize())) : 20;
        
        Sort sort = Sort.unsorted();
        if (criteria.getSortBy() != null && !criteria.getSortBy().trim().isEmpty()) {
            String sortBy = mapSortField(criteria.getSortBy().toLowerCase());
            Sort.Direction direction = "desc".equalsIgnoreCase(criteria.getSortDirection()) 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;
            sort = Sort.by(direction, sortBy);
        }
        
        return PageRequest.of(page, size, sort);
    }

    /**
     * Maps search sort fields to entity field names.
     */
    private String mapSortField(String sortBy) {
        return switch (sortBy) {
            case "name" -> "name";
            case "sku" -> "sku";
            case "unitprice" -> "unitPrice";
            case "quantityinstock" -> "quantityInStock";
            case "createdat" -> "createdAt";
            default -> "name"; // Default sort by name
        };
    }

    /**
     * Deletes a product by its ID.
     * @param productId the ID of the product to delete
     * @throws ProductNotFoundException if product is not found
     */
    public void deleteProduct(Long productId) {
        productValidator.validateForDeletion(productId);
        
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found with ID: " + productId);
        }
        
        productRepository.deleteById(productId);
    }

    /**
     * Checks if a product exists by SKU.
     * @param sku the SKU to check
     * @return true if product exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
        productValidator.validateSku(sku);
        return productRepository.existsBySku(sku);
    }

    /**
     * Updates product stock quantity.
     * @param productId the ID of the product
     * @param newQuantity the new stock quantity
     * @return the updated product as a response DTO
     * @throws ProductNotFoundException if product is not found
     */
    public ProductResponseDTO updateProductStock(Long productId, Integer newQuantity) {
        productValidator.validateProductId(productId);
        productValidator.validateQuantityInStock(newQuantity);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        
        product.setQuantityInStock(newQuantity);
        
        // Auto-update status based on stock
        if (newQuantity == 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        } else if (product.getStatus() == ProductStatus.OUT_OF_STOCK) {
            product.setStatus(ProductStatus.AVAILABLE);
        }
        
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }
}
