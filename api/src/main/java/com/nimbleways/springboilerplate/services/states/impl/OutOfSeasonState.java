package com.nimbleways.springboilerplate.services.states.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import com.nimbleways.springboilerplate.services.states.SeasonalProductState;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutOfSeasonState implements SeasonalProductState {
    private final NotificationService notificationService;
    private final ProductRepository productRepository;

    @Override
    public void handle(Product product) {
        notificationService.sendOutOfStockNotification(product.getName());
        product.setAvailable(0);
        productRepository.save(product);
    }
} 