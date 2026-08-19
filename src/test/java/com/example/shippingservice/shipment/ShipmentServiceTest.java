package com.example.shippingservice.shipment;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShipmentServiceTest {
    @Test
    void createsShipmentForPaidOrderOnlyOnce() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        ShipmentRepository shipments = mock(ShipmentRepository.class);
        when(shipments.findByOrderId(orderId)).thenReturn(Optional.empty());
        when(shipments.save(any(Shipment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ShipmentService service = new ShipmentService(shipments);

        Shipment shipment = service.createForPaidOrder(orderId, customerId);

        assertThat(shipment.getOrderId()).isEqualTo(orderId);
        verify(shipments).save(any(Shipment.class));
    }

    @Test
    void returnsExistingShipmentForRepeatedPaidOrderEvent() {
        UUID orderId = UUID.randomUUID();
        Shipment existing = Shipment.create(orderId, UUID.randomUUID());
        ShipmentRepository shipments = mock(ShipmentRepository.class);
        when(shipments.findByOrderId(orderId)).thenReturn(Optional.of(existing));
        ShipmentService service = new ShipmentService(shipments);

        Shipment shipment = service.createForPaidOrder(orderId, UUID.randomUUID());

        assertThat(shipment).isSameAs(existing);
        verify(shipments, never()).save(any());
    }

    @Test
    void listsShipmentsForOneCustomer() {
        UUID customerId = UUID.randomUUID();
        ShipmentRepository shipments = mock(ShipmentRepository.class);
        Shipment shipment = Shipment.create(UUID.randomUUID(), customerId);
        when(shipments.findByCustomerId(customerId)).thenReturn(List.of(shipment));
        ShipmentService service = new ShipmentService(shipments);

        assertThat(service.listForCustomer(customerId)).containsExactly(shipment);
    }

    @Test
    void dispatchesPendingShipment() {
        UUID orderId = UUID.randomUUID();
        Shipment shipment = Shipment.create(orderId, UUID.randomUUID());
        ShipmentRepository shipments = mock(ShipmentRepository.class);
        when(shipments.findByOrderId(orderId)).thenReturn(Optional.of(shipment));
        when(shipments.save(any(Shipment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ShipmentService service = new ShipmentService(shipments);

        Shipment dispatched = service.dispatch(orderId, "DHL", "TRACK-123");

        assertThat(dispatched.getStatus()).isEqualTo(ShipmentStatus.DISPATCHED);
        assertThat(dispatched.getCarrier()).isEqualTo("DHL");
        verify(shipments).save(shipment);
    }

    @Test
    void deliversDispatchedShipment() {
        UUID orderId = UUID.randomUUID();
        Shipment shipment = Shipment.create(orderId, UUID.randomUUID());
        shipment.dispatch("DHL", "TRACK-123");
        ShipmentRepository shipments = mock(ShipmentRepository.class);
        when(shipments.findByOrderId(orderId)).thenReturn(Optional.of(shipment));
        when(shipments.save(any(Shipment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ShipmentService service = new ShipmentService(shipments);

        Shipment delivered = service.deliver(orderId);

        assertThat(delivered.getStatus()).isEqualTo(ShipmentStatus.DELIVERED);
        verify(shipments).save(shipment);
    }

    @Test
    void rejectsDispatchForUnknownShipment() {
        ShipmentRepository shipments = mock(ShipmentRepository.class);
        UUID orderId = UUID.randomUUID();
        when(shipments.findByOrderId(orderId)).thenReturn(Optional.empty());
        ShipmentService service = new ShipmentService(shipments);

        assertThatThrownBy(() -> service.dispatch(orderId, "DHL", "TRACK-123"))
                .isInstanceOf(ShipmentNotFoundException.class);
    }
}
