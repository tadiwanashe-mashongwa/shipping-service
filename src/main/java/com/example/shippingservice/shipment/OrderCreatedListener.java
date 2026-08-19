package com.example.shippingservice.shipment;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {
    private final ShippingOrderService orders;
    public OrderCreatedListener(ShippingOrderService orders) { this.orders = orders; }
    @KafkaListener(topics = "order-created", groupId = "shipping-service")
    public void onOrderCreated(OrderCreatedEvent event) { orders.recordOrder(event.orderId(), event.customerId()); }
}
