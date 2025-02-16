package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.services.IProductDelayNotifier;
import com.nimbleways.springboilerplate.services.factories.SeasonalStateFactory;
import com.nimbleways.springboilerplate.services.factories.ExpirableStateFactory;
import com.nimbleways.springboilerplate.services.implementations.ProductService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@UnitTest
public class MyUnitTests {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private IProductDelayNotifier productDelayNotifier;
    @Mock
    private SeasonalStateFactory seasonalStateFactory;
    @Mock
    private ExpirableStateFactory expirableStateFactory;
    
    @InjectMocks 
    private ProductService productService;

    @Test
    public void test() {
        // GIVEN
        Product product = new Product(null, 15, 0, ProductType.NORMAL, "RJ45 Cable", null, null, null);
        int leadTime = 15;

        Mockito.when(productRepository.save(product)).thenReturn(product);

        // WHEN
        productService.notifyDelay(leadTime, product);

        // THEN
        assertEquals(0, product.getAvailable());
        assertEquals(15, product.getLeadTime());
        //Mockito.verify(productRepository, Mockito.times(1)).save(product);
        Mockito.verify(productDelayNotifier).notifyDelay(leadTime, product);
    }
}