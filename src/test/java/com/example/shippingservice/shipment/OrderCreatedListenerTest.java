package com.example.shippingservice.shipment;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.mockito.Mockito.*;

class OrderCreatedListenerTest {
 @Test void recordsEventOrderContext() {
  ShippingOrderService service = mock(ShippingOrderService.class);
  OrderCreatedEvent event = new OrderCreatedEvent(UUID.randomUUID(), UUID.randomUUID());
  new OrderCreatedListener(service).onOrderCreated(event);
  verify(service).recordOrder(event.orderId(), event.customerId());
 }
}
