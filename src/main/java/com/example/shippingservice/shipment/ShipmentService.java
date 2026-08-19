package com.example.shippingservice.shipment;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
public class ShipmentService {
    private final ShipmentRepository shipments;

    public ShipmentService(ShipmentRepository shipments) {
        this.shipments = shipments;
    }

    public Shipment createForPaidOrder(UUID orderId, UUID customerId) {
        return shipments.findByOrderId(orderId)
                .orElseGet(() -> shipments.save(Shipment.create(orderId, customerId)));
    }

    public List<Shipment> listForCustomer(UUID customerId) {
        return shipments.findByCustomerId(customerId);
    }

    public Shipment dispatch(UUID orderId, String carrier, String trackingNumber) {
        Shipment shipment = findByOrderId(orderId);
        shipment.dispatch(carrier, trackingNumber);
        return shipments.save(shipment);
    }

    public Shipment deliver(UUID orderId) {
        Shipment shipment = findByOrderId(orderId);
        shipment.deliver();
        return shipments.save(shipment);
    }

    private Shipment findByOrderId(UUID orderId) {
        return shipments.findByOrderId(orderId)
                .orElseThrow(() -> new ShipmentNotFoundException(orderId));
    }
}
