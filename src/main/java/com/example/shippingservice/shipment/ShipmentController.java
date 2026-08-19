package com.example.shippingservice.shipment;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shipments")
@Validated
public class ShipmentController {
    private final ShipmentService service;

    public ShipmentController(ShipmentService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public List<ShipmentResponse> listMine(@AuthenticationPrincipal Jwt jwt) {
        return service.listForCustomer(UUID.fromString(jwt.getSubject())).stream().map(ShipmentResponse::from).toList();
    }

    @PostMapping("/{orderId}/dispatch")
    public ShipmentResponse dispatch(
            @PathVariable UUID orderId,
            @RequestBody DispatchShipmentRequest request
    ) {
        return ShipmentResponse.from(service.dispatch(orderId, request.carrier(), request.trackingNumber()));
    }

    @PostMapping("/{orderId}/deliver")
    public ShipmentResponse deliver(@PathVariable UUID orderId) {
        return ShipmentResponse.from(service.deliver(orderId));
    }
}
