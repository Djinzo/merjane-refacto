package com.nimbleways.springboilerplate.services;

import com.nimbleways.springboilerplate.entities.Product;

public interface IProductDelayNotifier {
    void notifyDelay(int leadTime, Product product);
} 