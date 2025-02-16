package com.nimbleways.springboilerplate.services.states.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@UnitTest
class PreSeasonStateTests {
    @Mock
    private NotificationService notificationService;
    
    @Mock
    private ProductRepository productRepository;

    @Test
    void shouldNotifyAndSave() {
        // Given
        Product product = new Product(1L, 15, 5, ProductType.SEASONAL, "Watermelon", null, null, null);
        PreSeasonState state = new PreSeasonState(notificationService, productRepository);
        when(productRepository.save(product)).thenReturn(product);

        // When
        state.handle(product);

        // Then
        verify(notificationService).sendOutOfStockNotification(product.getName());
        verify(productRepository).save(product);
    }
} 