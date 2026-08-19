package com.example.shippingservice.shipment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShippingOrderRepository extends JpaRepository<ShippingOrder, UUID> {
}
