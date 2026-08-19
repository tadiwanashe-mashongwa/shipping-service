package com.example.shippingservice.shipment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentStatusChangedListener {
    private final ShippingOrderService orders;
    private final ObjectMapper objectMapper;

    public PaymentStatusChangedListener(ShippingOrderService orders, ObjectMapper objectMapper) {
        this.orders = orders;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "payment-status-changed", groupId = "shipping-service")
    public void onPaymentStatusChanged(String payload) {
        try {
            PaymentStatusChangedEvent event = objectMapper.readValue(payload, PaymentStatusChangedEvent.class);
            if ("SUCCESS".equals(event.status())) {
                orders.confirmSuccessfulPayment(event.orderId());
            }
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Invalid payment-status-changed event", exception);
        }
    }
}
