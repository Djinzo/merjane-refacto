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

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@UnitTest
class ExpiredOrUnavailableStateTests {
    @Mock
    private NotificationService notificationService;
    
    @Mock
    private ProductRepository productRepository;

    @Test
    void shouldSetAvailabilityToZeroAndNotify() {
        // Given
        LocalDate expiryDate = LocalDate.now().minusDays(1);
        Product product = new Product(1L, 15, 5, ProductType.EXPIRABLE, "Milk", expiryDate, null, null);
        ExpiredOrUnavailableState state = new ExpiredOrUnavailableState(notificationService, productRepository);
        when(productRepository.save(product)).thenReturn(product);

        // When
        state.handle(product);

        // Then
        assertEquals(0, product.getAvailable());
        verify(notificationService).sendExpirationNotification(product.getName(), expiryDate);
        verify(productRepository).save(product);
    }
} 