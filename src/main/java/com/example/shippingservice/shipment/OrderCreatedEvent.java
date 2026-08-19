package com.example.shippingservice.shipment;

import java.util.UUID;

public record OrderCreatedEvent(UUID orderId, UUID customerId) {
}
