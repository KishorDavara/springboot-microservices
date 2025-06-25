package com.microservices.order.service;

import com.microservices.order.client.InventoryClient;
import com.microservices.order.model.Order;
import com.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final InventoryClient inventoryClient;

    public Order placeOrder(Order orderRequest) {
        var isInStock = inventoryClient.isInStock(orderRequest.getSkuCode(), orderRequest.getQuantity());
        if (isInStock) {
            orderRequest.setOrderNumber(UUID.randomUUID().toString());
            Order order = repository.save(orderRequest);
            log.info("Order placed successfully.");
            return order;
        } else {
            throw new RuntimeException("Product with skuCode " + orderRequest.getSkuCode() + " is not in stock");
        }
    }

    public List<Order> fetchOrders() {
        return repository.findAll();
    }
}
