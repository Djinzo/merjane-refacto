package com.nimbleways.springboilerplate.services.states.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.services.IProductDelayNotifier;
import com.nimbleways.springboilerplate.services.states.SeasonalProductState;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InSeasonState implements SeasonalProductState {
    private final IProductDelayNotifier productDelayNotifier;

    @Override
    public void handle(Product product) {
        productDelayNotifier.notifyDelay(product.getLeadTime(), product);
    }
} 