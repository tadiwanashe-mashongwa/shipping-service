package com.example.shippingservice.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.mockito.Mockito.*;

class PaymentStatusChangedListenerTest {
 @Test void createsShipmentOnlyAfterSuccessfulPayment() {
  ShippingOrderService service = mock(ShippingOrderService.class);
  UUID orderId = UUID.randomUUID();
  String payload = "{\"paymentId\":\"" + UUID.randomUUID() + "\",\"orderId\":\"" + orderId + "\",\"status\":\"SUCCESS\"}";
  new PaymentStatusChangedListener(service, new ObjectMapper()).onPaymentStatusChanged(payload);
  verify(service).confirmSuccessfulPayment(orderId);
 }
}
