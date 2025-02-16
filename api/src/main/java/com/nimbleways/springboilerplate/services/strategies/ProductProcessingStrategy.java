package com.nimbleways.springboilerplate.services.strategies;

import com.nimbleways.springboilerplate.entities.Product;

public interface ProductProcessingStrategy {
    void processProduct(Product product);
    boolean supportsProductType(String productType);
} 