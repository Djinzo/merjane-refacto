package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.exceptions.OrderNotFoundException;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.services.factories.ProductProcessingStrategyFactory;
import com.nimbleways.springboilerplate.services.strategies.ProductProcessingStrategy;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@UnitTest
class OrderProcessingServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductProcessingStrategyFactory strategyFactory;

    @Mock
    private ProductProcessingStrategy productStrategy;

    @InjectMocks
    private OrderProcessingService orderProcessingService;

    private Order testOrder;
    private Set<Product> testProducts;

    @BeforeEach
    void setUp() {
        testProducts = new HashSet<>();
        testProducts.add(new Product(1L, 10, 5, ProductType.NORMAL, "Product 1", null, null, null));
        testProducts.add(new Product(2L, 15, 3, ProductType.SEASONAL, "Product 2", null, 
            LocalDate.now(), LocalDate.now().plusDays(30)));
        testProducts.add(new Product(3L, 20, 0, ProductType.EXPIRABLE, "Product 3", 
            LocalDate.now().plusDays(10), null, null));

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setItems(testProducts);
    }

    @Test
    void shouldProcessOrderSuccessfully() {
        // Given
        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(strategyFactory.getStrategy(any(ProductType.class))).thenReturn(productStrategy);

        // When
        Order result = orderProcessingService.processOrder(testOrder.getId());

        // Then
        assertEquals(testOrder.getId(), result.getId());
        verify(orderRepository).findById(testOrder.getId());
        verify(strategyFactory, times(testProducts.size())).getStrategy(any(ProductType.class));
        verify(productStrategy, times(testProducts.size())).processProduct(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Given
        Long nonExistentOrderId = 999L;
        when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(OrderNotFoundException.class, () -> 
            orderProcessingService.processOrder(nonExistentOrderId));
        verify(strategyFactory, never()).getStrategy(any(ProductType.class));
        verify(productStrategy, never()).processProduct(any(Product.class));
    }

    @Test
    void shouldProcessEmptyOrder() {
        // Given
        Order emptyOrder = new Order();
        emptyOrder.setId(2L);
        emptyOrder.setItems(new HashSet<>());
        when(orderRepository.findById(emptyOrder.getId())).thenReturn(java.util.Optional.of(emptyOrder));

        // When
        Order result = orderProcessingService.processOrder(emptyOrder.getId());

        // Then
        assertEquals(emptyOrder.getId(), result.getId());
        verify(orderRepository).findById(emptyOrder.getId());
        verify(strategyFactory, never()).getStrategy(any(ProductType.class));
        verify(productStrategy, never()).processProduct(any(Product.class));
    }

    @Test
    void shouldProcessDifferentProductTypes() {
        // Given
        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(strategyFactory.getStrategy(any(ProductType.class))).thenReturn(productStrategy);

        // When
        orderProcessingService.processOrder(testOrder.getId());

        // Then
        for (Product product : testProducts) {
            verify(strategyFactory).getStrategy(product.getType());
            verify(productStrategy).processProduct(product);
        }
    }

    @Test
    void shouldHandleStrategyFactoryError() {
        // Given
        when(orderRepository.findById(testOrder.getId())).thenReturn(java.util.Optional.of(testOrder));
        when(strategyFactory.getStrategy(any(ProductType.class)))
            .thenThrow(new IllegalArgumentException("Invalid product type"));

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> 
            orderProcessingService.processOrder(testOrder.getId()));
        verify(productStrategy, never()).processProduct(any(Product.class));
    }
} 