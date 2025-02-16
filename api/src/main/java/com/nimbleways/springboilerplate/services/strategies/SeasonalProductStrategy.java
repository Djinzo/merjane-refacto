package com.nimbleways.springboilerplate.services.strategies;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component("SEASONAL")
@RequiredArgsConstructor
public class SeasonalProductStrategy implements ProductProcessingStrategy {
    private final IProductService productService;

    @Override
    public void processProduct(Product product) {
        productService.handleSeasonalProduct(product);
    }

    @Override
    public boolean supportsProductType(String productType) {
        return "SEASONAL".equals(productType);
    }
} 