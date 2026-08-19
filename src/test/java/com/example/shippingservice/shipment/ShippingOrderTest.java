package com.example.shippingservice.shipment;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingOrderTest {
    @Test
    void recordsCustomerContextForAnOrderAwaitingPayment() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        ShippingOrder order = ShippingOrder.create(orderId, customerId);

        assertThat(order.getOrderId()).isEqualTo(orderId);
        assertThat(order.getCustomerId()).isEqualTo(customerId);
        assertThat(order.isPaymentConfirmed()).isFalse();
    }
}
