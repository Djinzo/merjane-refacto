package com.nimbleways.springboilerplate.services.strategies;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("EXPIRABLE")
@RequiredArgsConstructor
public class ExpirableProductStrategy implements ProductProcessingStrategy {
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public void processProduct(Product product) {
        if (product.getAvailable() > 0 && !product.getExpiryDate().isBefore(LocalDate.now())) {
            decrementAndSaveProduct(product);
        } else {
            productService.handleExpiredProduct(product);
        }
    }

    @Override
    public boolean supportsProductType(String productType) {
        return "EXPIRABLE".equals(productType);
    }

    private void decrementAndSaveProduct(Product product) {
        product.setAvailable(product.getAvailable() - 1);
        productRepository.save(product);
    }
} 