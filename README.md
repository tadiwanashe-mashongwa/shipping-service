# SpareLink Shipping Service

[![CI](https://github.com/tadiwanashe-mashongwa/shipping-service/actions/workflows/ci.yml/badge.svg)](https://github.com/tadiwanashe-mashongwa/shipping-service/actions/workflows/ci.yml)
[![JaCoCo coverage](https://github.com/tadiwanashe-mashongwa/shipping-service/raw/main/.github/badges/jacoco.svg)](https://github.com/tadiwanashe-mashongwa/shipping-service/actions/workflows/ci.yml)
[![Java 21](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot 3.5.5](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)](https://spring.io/projects/spring-boot)

Shipping microservice for **SpareLink**, an automotive spare-parts platform. It consumes order and payment lifecycle events, creates one shipment per paid order, and lets administrators dispatch and deliver shipments securely.

## Highlights

- Java 21, Spring Boot 3.5.5, PostgreSQL, Flyway, JPA/Hibernate
- Kafka-driven order context and successful-payment processing
- Idempotent shipment creation: one order produces at most one shipment
- Shipment lifecycle rules: `PENDING → DISPATCHED → DELIVERED`
- Keycloak JWT resource-server security with `CUSTOMER` and `ADMIN` roles
- OpenAPI/Swagger, Actuator, Docker, GitHub Actions and JaCoCo
- Testcontainers PostgreSQL persistence tests with real Flyway migrations

## Architecture

```mermaid
flowchart LR
    Client[Web / Mobile Client] -->|Bearer JWT| Shipping[shipping-service]
    Keycloak[Keycloak] -->|Issues JWT| Client
    Order[order-service] -->|order-created| Kafka[(Kafka)]
    Payment[payment-service] -->|payment-status-changed| Kafka
    Kafka --> Shipping
    Shipping -->|JPA + Flyway| Postgres[(shipping_db)]
```

## Shipment creation flow

```mermaid
sequenceDiagram
    participant O as order-service
    participant K as Kafka
    participant P as payment-service
    participant S as shipping-service
    participant DB as PostgreSQL

    O->>K: order-created(orderId, customerId, items)
    K->>S: Store shipping order context
    P->>K: payment-status-changed(orderId, SUCCESS)
    K->>S: Confirm successful payment
    S->>DB: Create one PENDING shipment
```

## Database model

```mermaid
erDiagram
    SHIPPING_ORDERS ||--o| SHIPMENTS : creates
    SHIPPING_ORDERS {
        uuid order_id PK
        uuid customer_id
        boolean payment_confirmed
        bigint version
    }
    SHIPMENTS {
        uuid id PK
        uuid order_id UK
        uuid customer_id
        varchar status
        varchar carrier
        varchar tracking_number
        bigint version
    }
```

## API

| Endpoint | Access | Purpose |
|---|---|---|
| `GET /api/shipments/me` | `CUSTOMER`, `ADMIN` | List the authenticated customer's shipments |
| `POST /api/shipments/{orderId}/dispatch` | `ADMIN` | Assign carrier/tracking number and dispatch a pending shipment |
| `POST /api/shipments/{orderId}/deliver` | `ADMIN` | Mark a dispatched shipment as delivered |
| `GET /actuator/health` | Public | Liveness/health check |

Dispatch request:

```json
{
  "carrier": "DHL",
  "trackingNumber": "TRACK-123"
}
```

Swagger UI: `http://localhost:8086/swagger-ui/index.html`

## Security and ownership

`GET /api/shipments/me` takes its customer identity from `JWT.sub`; clients cannot provide a customer ID. Keycloak realm roles are mapped to Spring `ROLE_CUSTOMER` and `ROLE_ADMIN`. Administrative lifecycle actions are denied to customers.

## Running locally

```powershell
mvn test
mvn spring-boot:run
```

The default local database is `shipping_db` on PostgreSQL port `5432`. In the platform stack, use Docker Compose from the parent `SpareLink` directory; the service is exposed on port `8086`.

## Testing strategy

The TDD suite covers domain transition rules, idempotent paid-order shipment creation, Kafka JSON listener contracts, customer ownership, admin authorization, controller mapping, and PostgreSQL/JPA/Flyway persistence through Testcontainers.

Run the full suite with `mvn test`. JaCoCo HTML reports are generated at `target/site/jacoco/index.html`; GitHub Actions publishes the coverage badge on pushes to `main`.

## Project structure

```text
shipment/  domain, services, Kafka listeners, REST API and persistence
config/    Keycloak security and OpenAPI configuration
db/        Flyway migrations
```
