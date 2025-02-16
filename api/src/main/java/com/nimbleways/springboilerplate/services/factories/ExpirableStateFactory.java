package com.nimbleways.springboilerplate.services.factories;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import com.nimbleways.springboilerplate.services.states.ExpirableProductState;
import com.nimbleways.springboilerplate.services.states.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ExpirableStateFactory {
    private final NotificationService notificationService;
    private final ProductRepository productRepository;

    public ExpirableProductState createState(Product product) {
        if (isProductAvailableAndNotExpired(product)) {
            return new AvailableState(productRepository);
        }
        return new ExpiredOrUnavailableState(notificationService, productRepository);
    }

    private boolean isProductAvailableAndNotExpired(Product product) {
        return product.getAvailable() > 0 && 
               product.getExpiryDate().isAfter(LocalDate.now());
    }
} 