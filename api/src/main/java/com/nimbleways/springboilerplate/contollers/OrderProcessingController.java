package com.nimbleways.springboilerplate.contollers;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.exceptions.OrderNotFoundException;
import com.nimbleways.springboilerplate.services.IOrderProcessingService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v2/orders")
@RequiredArgsConstructor
public class OrderProcessingController {
    private final IOrderProcessingService orderProcessingService;

    @PostMapping("/{orderId}/process")
    @ResponseStatus(HttpStatus.OK)
    public ProcessOrderResponse processOrder(@PathVariable Long orderId) {
        Order processedOrder = orderProcessingService.processOrder(orderId);
        return new ProcessOrderResponse(processedOrder.getId());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleOrderNotFound() {
    }
} 