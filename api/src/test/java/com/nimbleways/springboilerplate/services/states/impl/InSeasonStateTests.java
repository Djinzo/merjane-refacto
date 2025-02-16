package com.nimbleways.springboilerplate.services.states.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.services.IProductDelayNotifier;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@UnitTest
class InSeasonStateTests {
    @Mock
    private IProductDelayNotifier productDelayNotifier;

    @Test
    void shouldDelegateToDelayNotifier() {
        // Given
        Product product = new Product(1L, 15, 5, ProductType.SEASONAL, "Watermelon", null, null, null);
        InSeasonState state = new InSeasonState(productDelayNotifier);

        // When
        state.handle(product);

        // Then
        verify(productDelayNotifier).notifyDelay(product.getLeadTime(), product);
    }
} 