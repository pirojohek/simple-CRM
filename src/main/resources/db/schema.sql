CREATE SCHEMA storage;

create table storage.seller
(
    seller_id         SERIAL PRIMARY KEY,
    name              VARCHAR(512) NOT NULL,
    contact_info      VARCHAR(2048),
    registration_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

create table storage.transaction
(
    transaction_id   SERIAL PRIMARY KEY,
    seller_id        INTEGER        NOT NULL REFERENCES storage.seller (seller_id),
    amount           NUMERIC(12, 2) NOT NULL,
    payment_type     VARCHAR(32)    NOT NULL,
    transaction_date TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);