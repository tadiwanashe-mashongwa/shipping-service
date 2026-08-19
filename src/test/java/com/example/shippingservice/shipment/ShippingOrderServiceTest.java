package com.example.shippingservice.shipment;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShippingOrderServiceTest {
    @Test
    void recordsOrderCustomerContextOnce() {
        UUID orderId = UUID.randomUUID();
        ShippingOrderRepository orders = mock(ShippingOrderRepository.class);
        when(orders.findById(orderId)).thenReturn(Optional.empty());
        ShippingOrderService service = new ShippingOrderService(orders);

        service.recordOrder(orderId, UUID.randomUUID());

        verify(orders).save(any(ShippingOrder.class));
    }
}
