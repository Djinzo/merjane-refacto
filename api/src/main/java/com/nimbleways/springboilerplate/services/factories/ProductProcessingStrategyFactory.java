package com.nimbleways.springboilerplate.services.factories;

import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.services.strategies.ExpirableProductStrategy;
import com.nimbleways.springboilerplate.services.strategies.NormalProductStrategy;
import com.nimbleways.springboilerplate.services.strategies.ProductProcessingStrategy;
import com.nimbleways.springboilerplate.services.strategies.SeasonalProductStrategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductProcessingStrategyFactory {
    private final Map<String, ProductProcessingStrategy> strategies;

    public ProductProcessingStrategyFactory(
        NormalProductStrategy normalStrategy,
        SeasonalProductStrategy seasonalStrategy,
        ExpirableProductStrategy expirableStrategy
    ) {
        strategies = Map.of(
            ProductType.NORMAL.toString(), normalStrategy,
            ProductType.SEASONAL.toString(), seasonalStrategy,
            ProductType.EXPIRABLE.toString(), expirableStrategy
        );
    }

    public ProductProcessingStrategy getStrategy(ProductType type) {
        String typeStr = type.toString();
        if (!strategies.containsKey(typeStr)) {
            throw new IllegalArgumentException("No strategy found for product type: " + type);
        }
        return strategies.get(typeStr);
    }
} 