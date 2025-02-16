package com.nimbleways.springboilerplate.services.states;

import com.nimbleways.springboilerplate.entities.Product;

public interface ExpirableProductState {
    void handle(Product product);
} 