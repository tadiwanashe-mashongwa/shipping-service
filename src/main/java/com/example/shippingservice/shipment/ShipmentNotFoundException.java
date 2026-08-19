package com.example.shippingservice.shipment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShipmentNotFoundException extends RuntimeException {
    public ShipmentNotFoundException(UUID orderId) {
        super("Shipment not found for order " + orderId);
    }
}
