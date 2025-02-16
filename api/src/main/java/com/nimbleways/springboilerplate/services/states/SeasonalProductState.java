package com.nimbleways.springboilerplate.services.states;

import com.nimbleways.springboilerplate.entities.Product;

public interface SeasonalProductState {
    void handle(Product product);
} 