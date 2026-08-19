package com.example.shippingservice.shipment;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShippingOrderService {
    private final ShippingOrderRepository orders;
    private final ShipmentService shipments;

    public ShippingOrderService(ShippingOrderRepository orders, ShipmentService shipments) {
        this.orders = orders;
        this.shipments = shipments;
    }

    public void recordOrder(UUID orderId, UUID customerId) {
        if (orders.findById(orderId).isEmpty()) {
            orders.save(ShippingOrder.create(orderId, customerId));
        }
    }

    public void confirmSuccessfulPayment(UUID orderId) {
        orders.findById(orderId).ifPresent(order -> {
            order.confirmPayment();
            orders.save(order);
            shipments.createForPaidOrder(order.getOrderId(), order.getCustomerId());
        });
    }
}
