package com.example.shippingservice.shipment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {
    private final ShippingOrderService orders;
    private final ObjectMapper objectMapper;

    public OrderCreatedListener(ShippingOrderService orders, ObjectMapper objectMapper) {
        this.orders = orders;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-created", groupId = "shipping-service")
    public void onOrderCreated(String payload) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(payload, OrderCreatedEvent.class);
            orders.recordOrder(event.orderId(), event.customerId());
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Invalid order-created event", exception);
        }
    }
}
