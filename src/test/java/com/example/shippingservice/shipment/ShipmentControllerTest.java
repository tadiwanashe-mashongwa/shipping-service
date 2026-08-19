package com.example.shippingservice.shipment;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ShipmentControllerTest {
    @Test
    void listsShipmentsForAuthenticatedCustomer() {
        UUID customerId = UUID.randomUUID();
        ShipmentService service = mock(ShipmentService.class);
        when(service.listForCustomer(customerId)).thenReturn(List.of(Shipment.create(UUID.randomUUID(), customerId)));
        ShipmentController controller = new ShipmentController(service);
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"), Map.of("sub", customerId.toString()));

        List<ShipmentResponse> response = controller.listMine(jwt);

        assertThat(response).hasSize(1);
        verify(service).listForCustomer(customerId);
    }

    @Test
    void dispatchesShipmentForAdministrator() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        Shipment shipment = Shipment.create(orderId, customerId);
        shipment.dispatch("DHL", "TRACK-123");
        ShipmentService service = mock(ShipmentService.class);
        when(service.dispatch(orderId, "DHL", "TRACK-123")).thenReturn(shipment);
        ShipmentController controller = new ShipmentController(service);

        ShipmentResponse response = controller.dispatch(orderId, new DispatchShipmentRequest("DHL", "TRACK-123"));

        assertThat(response.status()).isEqualTo(ShipmentStatus.DISPATCHED);
        verify(service).dispatch(orderId, "DHL", "TRACK-123");
    }
}
