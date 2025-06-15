package com.microservices.order.service;

import com.microservices.order.model.Order;
import com.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    public Order placeOrder(Order orderRequest) {
        orderRequest.setOrderNumber(UUID.randomUUID().toString());
        Order order = repository.save(orderRequest);
        log.info("Order created successfully.");
        return order;
    }
}
