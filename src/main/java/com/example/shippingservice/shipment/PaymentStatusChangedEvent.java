package com.example.shippingservice.shipment;

import java.util.UUID;

public record PaymentStatusChangedEvent(UUID paymentId, UUID orderId, String status) {
}
