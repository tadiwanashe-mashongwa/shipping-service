package com.example.shippingservice.shipment;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class ShippingOrderServiceTest {
    @Test
    void recordsOrderCustomerContextOnce() {
        UUID orderId = UUID.randomUUID();
        ShippingOrderRepository orders = mock(ShippingOrderRepository.class);
        when(orders.findById(orderId)).thenReturn(Optional.empty());
        ShippingOrderService service = new ShippingOrderService(orders, mock(ShipmentService.class));

        service.recordOrder(orderId, UUID.randomUUID());

        verify(orders).save(any(ShippingOrder.class));
    }

    @Test
    void createsShipmentWhenStoredOrderPaymentSucceeds() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        ShippingOrder order = ShippingOrder.create(orderId, customerId);
        ShippingOrderRepository orders = mock(ShippingOrderRepository.class);
        ShipmentService shipments = mock(ShipmentService.class);
        when(orders.findById(orderId)).thenReturn(Optional.of(order));
        ShippingOrderService service = new ShippingOrderService(orders, shipments);

        service.confirmSuccessfulPayment(orderId);

        assertThat(order.isPaymentConfirmed()).isTrue();
        verify(shipments).createForPaidOrder(orderId, customerId);
    }
}
