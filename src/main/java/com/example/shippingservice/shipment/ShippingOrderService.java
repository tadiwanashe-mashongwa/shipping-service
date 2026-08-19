package com.example.shippingservice.shipment;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShippingOrderService {
    private final ShippingOrderRepository orders;

    public ShippingOrderService(ShippingOrderRepository orders) {
        this.orders = orders;
    }

    public void recordOrder(UUID orderId, UUID customerId) {
        if (orders.findById(orderId).isEmpty()) {
            orders.save(ShippingOrder.create(orderId, customerId));
        }
    }
}
