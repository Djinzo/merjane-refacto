package com.nimbleways.springboilerplate.services;

import com.nimbleways.springboilerplate.entities.Order;

public interface IOrderProcessingService {
    Order processOrder(Long orderId);
} 