package com.example.shippingservice.shipment;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.mockito.Mockito.*;

class PaymentStatusChangedListenerTest {
 @Test void createsShipmentOnlyAfterSuccessfulPayment() {
  ShippingOrderService service = mock(ShippingOrderService.class);
  UUID orderId = UUID.randomUUID();
  new PaymentStatusChangedListener(service).onPaymentStatusChanged(new PaymentStatusChangedEvent(UUID.randomUUID(), orderId, "SUCCESS"));
  verify(service).confirmSuccessfulPayment(orderId);
 }
}
