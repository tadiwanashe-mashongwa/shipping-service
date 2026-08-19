package com.example.shippingservice.shipment;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentStatusChangedListener {
    private final ShippingOrderService orders;
    public PaymentStatusChangedListener(ShippingOrderService orders) { this.orders = orders; }
    @KafkaListener(topics = "payment-status-changed", groupId = "shipping-service")
    public void onPaymentStatusChanged(PaymentStatusChangedEvent event) {
        if ("SUCCESS".equals(event.status())) orders.confirmSuccessfulPayment(event.orderId());
    }
}
