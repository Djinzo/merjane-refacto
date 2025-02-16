package com.nimbleways.springboilerplate.services.factories;

import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.services.strategies.ProductProcessingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductProcessingStrategyFactory {
    private final Map<ProductType, ProductProcessingStrategy> strategyMap;

    public ProductProcessingStrategy getStrategy(ProductType productType) {
        ProductProcessingStrategy strategy = strategyMap.get(productType);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for product type: " + productType);
        }
        return strategy;
    }
} 