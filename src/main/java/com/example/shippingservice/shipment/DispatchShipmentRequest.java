package com.example.shippingservice.shipment;

import jakarta.validation.constraints.NotBlank;

public record DispatchShipmentRequest(
        @NotBlank String carrier,
        @NotBlank String trackingNumber
) {
}
