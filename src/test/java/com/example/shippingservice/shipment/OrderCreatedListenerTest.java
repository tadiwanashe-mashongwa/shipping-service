package com.example.shippingservice.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.mockito.Mockito.*;

class OrderCreatedListenerTest {
 @Test void recordsEventOrderContext() {
  ShippingOrderService service = mock(ShippingOrderService.class);
  OrderCreatedEvent event = new OrderCreatedEvent(UUID.randomUUID(), UUID.randomUUID());
  String payload = "{\"orderId\":\"" + event.orderId() + "\",\"customerId\":\"" + event.customerId() + "\",\"items\":[]}";
  new OrderCreatedListener(service, new ObjectMapper()).onOrderCreated(payload);
  verify(service).recordOrder(event.orderId(), event.customerId());
 }
}
