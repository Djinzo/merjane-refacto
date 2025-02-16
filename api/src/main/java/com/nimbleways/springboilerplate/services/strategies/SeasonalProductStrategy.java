package com.nimbleways.springboilerplate.services.strategies;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("SEASONAL")
@RequiredArgsConstructor
public class SeasonalProductStrategy implements ProductProcessingStrategy {
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public void processProduct(Product product) {
        LocalDate now = LocalDate.now();
        if (isInSeason(product, now) && product.getAvailable() > 0) {
            decrementAndSaveProduct(product);
        } else {
            productService.handleSeasonalProduct(product);
        }
    }

    @Override
    public boolean supportsProductType(String productType) {
        return "SEASONAL".equals(productType);
    }

    private boolean isInSeason(Product product, LocalDate date) {
        return date.isAfter(product.getSeasonStartDate()) && 
               date.isBefore(product.getSeasonEndDate());
    }

    private void decrementAndSaveProduct(Product product) {
        product.setAvailable(product.getAvailable() - 1);
        productRepository.save(product);
    }
} 