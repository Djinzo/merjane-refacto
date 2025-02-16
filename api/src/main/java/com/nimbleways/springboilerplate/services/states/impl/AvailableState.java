package com.nimbleways.springboilerplate.services.states.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.states.ExpirableProductState;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AvailableState implements ExpirableProductState {
    private final ProductRepository productRepository;

    @Override
    public void handle(Product product) {
        product.setAvailable(product.getAvailable() - 1);
        productRepository.save(product);
    }
} 