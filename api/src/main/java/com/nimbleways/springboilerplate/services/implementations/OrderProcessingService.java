package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.exceptions.OrderNotFoundException;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.services.IOrderProcessingService;
import com.nimbleways.springboilerplate.services.factories.ProductProcessingStrategyFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderProcessingService implements IOrderProcessingService {
    private final OrderRepository orderRepository;
    private final ProductProcessingStrategyFactory strategyFactory;

    @Transactional
    public Order processOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        Set<Product> products = order.getItems();
        products.forEach(this::processProduct);

        return order;
    }

    private void processProduct(Product product) {
        strategyFactory.getStrategy(product.getType())
                      .processProduct(product);
    }
} 