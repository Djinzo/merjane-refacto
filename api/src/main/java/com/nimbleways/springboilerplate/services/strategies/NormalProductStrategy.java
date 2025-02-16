package com.nimbleways.springboilerplate.services.strategies;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("NORMAL")
@RequiredArgsConstructor
public class NormalProductStrategy implements ProductProcessingStrategy {
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public void processProduct(Product product) {
        if (product.getAvailable() > 0) {
            decrementAndSaveProduct(product);
        } else if (product.getLeadTime() > 0) {
            productService.notifyDelay(product.getLeadTime(), product);
        }
    }

    @Override
    public boolean supportsProductType(String productType) {
        return "NORMAL".equals(productType);
    }

    private void decrementAndSaveProduct(Product product) {
        product.setAvailable(product.getAvailable() - 1);
        productRepository.save(product);
    }
} 