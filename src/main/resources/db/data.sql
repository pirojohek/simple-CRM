INSERT INTO storage.seller (name, contact_info)
VALUES ('Ivan Petrov', 'ivan.petrov@example.com'),
       ('Olga Sidorova', 'olga.sidorova@example.com'),
       ('Sergey Ivanov', 'sergey.ivanov@example.com'),
       ('Anna Smirnova', 'anna.smirnova@example.com'),
       ('Dmitry Kuznetsov', 'dmitry.kuznetsov@example.com');

INSERT INTO storage.transaction (seller_id, amount, payment_type, transaction_date)
VALUES (1, 1000.00, 'CARD', '2026-01-01 10:15:00'),
       (1, 500.50, 'CASH', '2026-01-01 14:30:00'),
       (1, 200.75, 'TRANSFER', '2026-01-02 09:00:00'),
       (1, 1500.00, 'CARD', '2026-01-05 16:45:00'),
       (1, 300.00, 'CASH', '2026-02-01 12:00:00');

INSERT INTO storage.transaction (seller_id, amount, payment_type, transaction_date)
VALUES (2, 50.00, 'CASH', '2026-01-01 11:00:00'),
       (2, 75.25, 'CARD', '2026-01-03 13:30:00'),
       (2, 30.00, 'TRANSFER', '2026-01-10 15:00:00');

INSERT INTO storage.transaction (seller_id, amount, payment_type, transaction_date)
VALUES (3, 2000.00, 'CARD', '2026-01-01 09:00:00'),
       (3, 1800.00, 'TRANSFER', '2026-01-02 10:30:00'),
       (3, 2200.50, 'CASH', '2026-01-04 15:00:00'),
       (3, 2500.00, 'CARD', '2026-01-10 11:15:00');

INSERT INTO storage.transaction (seller_id, amount, payment_type, transaction_date)
VALUES (4, 300.00, 'CARD', '2026-01-05 10:00:00'),
       (4, 400.00, 'CASH', '2026-01-06 12:30:00'),
       (4, 150.00, 'TRANSFER', '2026-01-07 14:00:00');

INSERT INTO storage.transaction (seller_id, amount, payment_type, transaction_date)
VALUES (5, 600.00, 'CARD', '2026-01-02 09:30:00'),
       (5, 700.00, 'TRANSFER', '2026-01-03 15:00:00'),
       (5, 800.00, 'CASH', '2026-01-04 16:00:00');