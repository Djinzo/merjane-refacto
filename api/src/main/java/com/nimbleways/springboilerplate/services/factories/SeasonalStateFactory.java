package com.nimbleways.springboilerplate.services.factories;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.IProductDelayNotifier;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import com.nimbleways.springboilerplate.services.states.SeasonalProductState;
import com.nimbleways.springboilerplate.services.states.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SeasonalStateFactory {
    private final NotificationService notificationService;
    private final ProductRepository productRepository;
    private final IProductDelayNotifier productDelayNotifier;

    public SeasonalProductState createState(Product product) {
        LocalDate now = LocalDate.now();
        LocalDate deliveryDate = now.plusDays(product.getLeadTime());

        if (isDeliveryAfterSeasonEnd(deliveryDate, product)) {
            return new OutOfSeasonState(notificationService, productRepository);
        }
        if (isBeforeSeasonStart(now, product)) {
            return new PreSeasonState(notificationService, productRepository);
        }
        return new InSeasonState(productDelayNotifier);
    }

    private boolean isDeliveryAfterSeasonEnd(LocalDate deliveryDate, Product product) {
        return deliveryDate.isAfter(product.getSeasonEndDate());
    }

    private boolean isBeforeSeasonStart(LocalDate currentDate, Product product) {
        return product.getSeasonStartDate().isAfter(currentDate);
    }
} 