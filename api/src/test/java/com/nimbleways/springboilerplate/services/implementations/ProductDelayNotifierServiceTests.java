package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@UnitTest
class ProductDelayNotifierServiceTests {
    @Mock
    private NotificationService notificationService;
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductDelayNotifierService productDelayNotifier;

    @Test
    void shouldUpdateLeadTimeAndNotify() {
        // Given
        Product product = new Product(1L, 0, 0, ProductType.NORMAL, "RJ45 Cable", null, null, null);
        int leadTime = 15;
        when(productRepository.save(product)).thenReturn(product);

        // When
        productDelayNotifier.notifyDelay(leadTime, product);

        // Then
        verify(productRepository).save(product);
        verify(notificationService).sendDelayNotification(leadTime, product.getName());
    }
} 