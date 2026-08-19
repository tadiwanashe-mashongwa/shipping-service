CREATE TABLE shipping_orders (
  order_id UUID PRIMARY KEY,
  customer_id UUID NOT NULL,
  payment_confirmed BOOLEAN NOT NULL DEFAULT FALSE,
  version BIGINT NOT NULL
);
