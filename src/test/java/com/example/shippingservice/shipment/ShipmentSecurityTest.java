package com.example.shippingservice.shipment;

import com.example.shippingservice.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShipmentController.class)
@Import(SecurityConfig.class)
class ShipmentSecurityTest {
    @Autowired private MockMvc mvc;
    @MockitoBean private ShipmentService service;

    @Test
    void rejectsAnonymousShipmentRequests() throws Exception {
        mvc.perform(get("/api/shipments/me")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void rejectsCustomerDispatchRequests() throws Exception {
        mvc.perform(post("/api/shipments/" + java.util.UUID.randomUUID() + "/dispatch")
                        .contentType("application/json")
                        .content("{\"carrier\":\"DHL\",\"trackingNumber\":\"TRACK-123\"}"))
                .andExpect(status().isForbidden());
    }
}
