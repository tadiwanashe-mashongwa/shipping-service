CREATE TABLE shipments (
  id UUID PRIMARY KEY,
  order_id UUID NOT NULL UNIQUE,
  customer_id UUID NOT NULL,
  status VARCHAR(30) NOT NULL,
  version BIGINT NOT NULL
);
