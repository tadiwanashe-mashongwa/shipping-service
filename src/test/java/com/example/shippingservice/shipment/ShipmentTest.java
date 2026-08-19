package com.example.shippingservice.shipment;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentTest {
    @Test
    void createsPendingShipmentForPaidOrder() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Shipment shipment = Shipment.create(orderId, customerId);

        assertThat(shipment.getOrderId()).isEqualTo(orderId);
        assertThat(shipment.getCustomerId()).isEqualTo(customerId);
        assertThat(shipment.getStatus()).isEqualTo(ShipmentStatus.PENDING);
    }

    @Test
    void dispatchesPendingShipmentWithCarrierAndTrackingNumber() {
        Shipment shipment = Shipment.create(UUID.randomUUID(), UUID.randomUUID());

        shipment.dispatch("DHL", "TRACK-123");

        assertThat(shipment.getStatus()).isEqualTo(ShipmentStatus.DISPATCHED);
        assertThat(shipment.getCarrier()).isEqualTo("DHL");
        assertThat(shipment.getTrackingNumber()).isEqualTo("TRACK-123");
    }

    @Test
    void deliversDispatchedShipment() {
        Shipment shipment = Shipment.create(UUID.randomUUID(), UUID.randomUUID());
        shipment.dispatch("DHL", "TRACK-123");

        shipment.deliver();

        assertThat(shipment.getStatus()).isEqualTo(ShipmentStatus.DELIVERED);
    }
}
