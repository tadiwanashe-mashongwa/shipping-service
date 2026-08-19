package com.example.shippingservice.shipment;

import java.util.UUID;

public record ShipmentResponse(UUID orderId, ShipmentStatus status, String carrier, String trackingNumber) {
    static ShipmentResponse from(Shipment shipment) {
        return new ShipmentResponse(shipment.getOrderId(), shipment.getStatus(), shipment.getCarrier(), shipment.getTrackingNumber());
    }
}
