package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.IProductDelayNotifier;
import com.nimbleways.springboilerplate.services.factories.ExpirableStateFactory;
import com.nimbleways.springboilerplate.services.factories.SeasonalStateFactory;
import com.nimbleways.springboilerplate.services.states.ExpirableProductState;
import com.nimbleways.springboilerplate.services.states.SeasonalProductState;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
@ExtendWith(SpringExtension.class)
@UnitTest
class ProductServiceTests {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private SeasonalStateFactory seasonalStateFactory;
    @Mock
    private ExpirableStateFactory expirableStateFactory;
    @Mock
    private IProductDelayNotifier productDelayNotifier;
    
    @InjectMocks 
    private ProductService productService;

    @Nested
    class NotifyDelayTests {
        @Test
        void shouldDelegateToDelayNotifier() {
            // Given
            Product product = new Product(1L, 15, 0, ProductType.NORMAL, "RJ45 Cable", null, null, null);
            int leadTime = 15;

            // When
            productService.notifyDelay(leadTime, product);

            // Then
            verify(productDelayNotifier).notifyDelay(leadTime, product);
        }
    }

    @Nested
    class HandleSeasonalProductTests {
        private Product seasonalProduct;
        private SeasonalProductState seasonalState;

        @BeforeEach
        void setUp() {
            LocalDate now = LocalDate.now();
            seasonalProduct = new Product(1L, 10, 5, ProductType.SEASONAL, "Watermelon", 
                null, now.minusDays(5), now.plusDays(30));
            seasonalState = mock(SeasonalProductState.class);
        }

        @Test
        void shouldHandleProductInSeason() {
            // Given
            when(seasonalStateFactory.createState(seasonalProduct)).thenReturn(seasonalState);

            // When
            productService.handleSeasonalProduct(seasonalProduct);

            // Then
            verify(seasonalState).handle(seasonalProduct);
        }

        @Test
        void shouldHandleOutOfSeasonProduct() {
            // Given
            seasonalProduct.setSeasonEndDate(LocalDate.now().minusDays(1));
            when(seasonalStateFactory.createState(seasonalProduct)).thenReturn(seasonalState);

            // When
            productService.handleSeasonalProduct(seasonalProduct);

            // Then
            verify(seasonalState).handle(seasonalProduct);
        }

        @Test
        void shouldHandlePreSeasonProduct() {
            // Given
            seasonalProduct.setSeasonStartDate(LocalDate.now().plusDays(10));
            when(seasonalStateFactory.createState(seasonalProduct)).thenReturn(seasonalState);

            // When
            productService.handleSeasonalProduct(seasonalProduct);

            // Then
            verify(seasonalState).handle(seasonalProduct);
        }
    }

    @Nested
    class HandleExpiredProductTests {
        private Product expirableProduct;
        private ExpirableProductState expirableState;

        @BeforeEach
        void setUp() {
            LocalDate now = LocalDate.now();
            expirableProduct = new Product(1L, 10, 5, ProductType.EXPIRABLE, "Milk", 
                now.plusDays(5), null, null);
            expirableState = mock(ExpirableProductState.class);
        }

        @Test
        void shouldHandleNonExpiredProduct() {
            // Given
            when(expirableStateFactory.createState(expirableProduct)).thenReturn(expirableState);

            // When
            productService.handleExpiredProduct(expirableProduct);

            // Then
            verify(expirableState).handle(expirableProduct);
        }

        @Test
        void shouldHandleExpiredProduct() {
            // Given
            expirableProduct.setExpiryDate(LocalDate.now().minusDays(1));
            when(expirableStateFactory.createState(expirableProduct)).thenReturn(expirableState);

            // When
            productService.handleExpiredProduct(expirableProduct);

            // Then
            verify(expirableState).handle(expirableProduct);
        }

        @Test
        void shouldHandleUnavailableProduct() {
            // Given
            expirableProduct.setAvailable(0);
            when(expirableStateFactory.createState(expirableProduct)).thenReturn(expirableState);

            // When
            productService.handleExpiredProduct(expirableProduct);

            // Then
            verify(expirableState).handle(expirableProduct);
        }
    }
}