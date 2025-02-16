package com.nimbleways.springboilerplate.services.implementations;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.services.IProductService;
import com.nimbleways.springboilerplate.services.IProductDelayNotifier;
import com.nimbleways.springboilerplate.services.factories.SeasonalStateFactory;
import com.nimbleways.springboilerplate.services.factories.ExpirableStateFactory;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final SeasonalStateFactory seasonalStateFactory;
    private final ExpirableStateFactory expirableStateFactory;
    private final IProductDelayNotifier productDelayNotifier;

    @Override
    public void notifyDelay(int leadTime, Product product) {
        productDelayNotifier.notifyDelay(leadTime, product);
    }

    @Override
    public void handleSeasonalProduct(Product product) {
        seasonalStateFactory.createState(product).handle(product);
    }

    @Override
    public void handleExpiredProduct(Product product) {
        expirableStateFactory.createState(product).handle(product);
    }
}