package com.nimbleways.springboilerplate.services.states.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@UnitTest
class AvailableStateTests {
    @Mock
    private ProductRepository productRepository;

    @Test
    void shouldDecrementAvailabilityAndSave() {
        // Given
        Product product = new Product(1L, 15, 5, ProductType.EXPIRABLE, "Milk", null, null, null);
        AvailableState state = new AvailableState(productRepository);
        when(productRepository.save(product)).thenReturn(product);

        // When
        state.handle(product);

        // Then
        assertEquals(4, product.getAvailable());
        verify(productRepository).save(product);
    }
} 