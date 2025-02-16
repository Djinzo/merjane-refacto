package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderProcessingControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private List<Product> testProducts;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testProducts = createProducts();
        productRepository.saveAll(testProducts);
        
        Set<Product> orderItems = new HashSet<>(testProducts);
        testOrder = createOrder(orderItems);
        testOrder = orderRepository.save(testOrder);
    }

    @Test
    void shouldProcessOrderSuccessfully() throws Exception {
        // Get initial quantity
        Product initialProduct = testProducts.get(0); // USB Cable
        int initialQuantity = initialProduct.getAvailable();

        // Act
        mockMvc.perform(post("/v2/orders/{orderId}/process", testOrder.getId())
                .contentType("application/json"))
                .andExpect(status().isOk());

        // Assert
        Product updatedProduct = productRepository.findById(initialProduct.getId()).get();
        assertEquals(initialQuantity - 1, updatedProduct.getAvailable(),
            String.format("Available quantity should be decremented for product: %s", initialProduct.getName()));
    }

    @Test
    void shouldHandleNonExistentOrder() throws Exception {
        mockMvc.perform(post("/v2/orders/{orderId}/process", 999L)
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldHandleOutOfStockProducts() throws Exception {
        // Arrange
        //Product outOfStockProduct = testProducts.get(1); // USB Dongle with 0 quantity
        
        // Act
        mockMvc.perform(post("/v2/orders/{orderId}/process", testOrder.getId())
                .contentType("application/json"))
                .andExpect(status().isOk());

        // Assert
        verify(notificationService, times(1))
            .sendDelayNotification(anyInt(), anyString());
    }

    @Test
    void shouldHandleExpiredProducts() throws Exception {
        // Arrange
        Product expiredProduct = testProducts.get(3); // Expired milk
        
        // Act
        mockMvc.perform(post("/v2/orders/{orderId}/process", testOrder.getId())
                .contentType("application/json"))
                .andExpect(status().isOk());

        // Assert
        verify(notificationService, times(1))
            .sendExpirationNotification(anyString(), any(LocalDate.class));
    }

    private static Order createOrder(Set<Product> products) {
        Order order = new Order();
        order.setItems(products);
        return order;
    }

    private static List<Product> createProducts() {
        List<Product> products = new ArrayList<>();
        
        // Normal products
        products.add(new Product(null, 15, 30, ProductType.NORMAL, "USB Cable", null, null, null));
        products.add(new Product(null, 10, 0, ProductType.NORMAL, "USB Dongle", null, null, null));
        
        // Expirable products
        products.add(new Product(null, 15, 30, ProductType.EXPIRABLE, "Butter", 
            LocalDate.now().plusDays(26), null, null));
        products.add(new Product(null, 90, 6, ProductType.EXPIRABLE, "Milk", 
            LocalDate.now().minusDays(2), null, null));
        
        // Seasonal products
        products.add(new Product(null, 15, 30, ProductType.SEASONAL, "Watermelon", null,
            LocalDate.now().minusDays(2), LocalDate.now().plusDays(58)));
        products.add(new Product(null, 15, 30, ProductType.SEASONAL, "Grapes", null,
            LocalDate.now().plusDays(180), LocalDate.now().plusDays(240)));
            
        return products;
    }
} 