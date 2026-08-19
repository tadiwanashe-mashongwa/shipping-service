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
}
