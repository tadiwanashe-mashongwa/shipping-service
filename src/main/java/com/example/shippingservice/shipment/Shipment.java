package com.example.shippingservice.shipment;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.util.UUID;

@Entity
@Table(name = "shipments")
public class Shipment {
    @Id private UUID id;
    private UUID orderId;
    private UUID customerId;
    @Enumerated(EnumType.STRING) private ShipmentStatus status;
    @Version private long version;

    protected Shipment() { }

    private Shipment(UUID orderId, UUID customerId) {
        this.id = UUID.randomUUID();
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = ShipmentStatus.PENDING;
    }

    public static Shipment create(UUID orderId, UUID customerId) {
        return new Shipment(orderId, customerId);
    }

    public UUID getOrderId() { return orderId; }
    public UUID getCustomerId() { return customerId; }
    public ShipmentStatus getStatus() { return status; }
}
