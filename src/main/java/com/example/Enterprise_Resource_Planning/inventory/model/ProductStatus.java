package com.example.Enterprise_Resource_Planning.inventory.model;

/**
 * Enumeration representing the various states a product can be in.
 */
public enum ProductStatus {
    AVAILABLE("Available"),
    OUT_OF_STOCK("Out of Stock"),
    DISCONTINUED("Discontinued"),
    PENDING_RESTOCK("Pending Restock"),
    PRE_ORDER("Pre-Order"),
    BACK_ORDER("Back Order"),
    INACTIVE("Inactive"),
    DRAFT("Draft");
    
    private final String displayName;
    
    ProductStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Check if the product status indicates the item is sellable.
     * 
     * @return true if the product can be sold
     */
    public boolean isSellable() {
        return this == AVAILABLE || this == PRE_ORDER || this == BACK_ORDER;
    }
    
    /**
     * Check if the product status indicates the item is active in inventory.
     * 
     * @return true if the product is active
     */
    public boolean isActive() {
        return this != DISCONTINUED && this != INACTIVE && this != DRAFT;
    }
}
