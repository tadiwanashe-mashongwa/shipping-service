package com.example.shippingservice.shipment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.util.UUID;

@Entity
@Table(name = "shipping_orders")
public class ShippingOrder {
    @Id private UUID orderId;
    private UUID customerId;
    private boolean paymentConfirmed;
    @Version private long version;

    protected ShippingOrder() { }

    private ShippingOrder(UUID orderId, UUID customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
    }

    public static ShippingOrder create(UUID orderId, UUID customerId) {
        return new ShippingOrder(orderId, customerId);
    }

    public UUID getOrderId() { return orderId; }
    public UUID getCustomerId() { return customerId; }
    public boolean isPaymentConfirmed() { return paymentConfirmed; }
    public void confirmPayment() { this.paymentConfirmed = true; }
}
