INSERT INTO SUPPLIER(id, name) VALUES
 (1, 'S1'), 
 (2, 'S2'), 
 (3, 'S3');

INSERT INTO PRODUCT(id, name) VALUES
 (1, 'Яблоки красные'),
 (2, 'Яблоки жёлтые'),
 (3, 'Яблоки зелёные'),
 (4, 'Груши жёлтые'),
 (5, 'Груши зелёные'),
 (6, 'Груши красные');

INSERT INTO PERIOD(id, name) VALUES
 (1, '2020'),
 (2, '2021'),
 (3, '2022'),
 (4, '2023'),
 (5, '2024');

INSERT INTO PRICE(id, supplier_id, period_id) VALUES
 (1, 1, 3),
 (2, 2, 3),
 (3, 3, 3),
 (4, 1, 4),
 (5, 2, 4),
 (6, 3, 4),
 (7, 1, 5),
 (8, 2, 5);

INSERT INTO PRICE_PRODUCT(price_id, product_id, value) VALUES
 (1, 1, 100000),
 (1, 2, 125000),
 (1, 4, 150000),
 (1, 5, 175000),
 (2, 2, 120000),
 (2, 3, 150000),
 (2, 5, 170000),
 (2, 6, 180000),
 (3, 1, 110000),
 (3, 3, 140000),
 (3, 4, 160000),
 (3, 6, 175000);

INSERT INTO SHIPMENT(id, price_id) VALUES
 (1, 1),
 (2, 1),
 (3, 1),
 (4, 2),
 (5, 2),
 (6, 2),
 (7, 3),
 (8, 3),
 (9, 3),
 (10, 4),
 (11, 4);

INSERT INTO SHIPMENT_PRODUCT(shipment_id, price_id, product_id, quantity) VALUES
 (1, 1, 1, 1),
 (1, 1, 2, 1.5),
 (1, 1, 5, 1.3),
 (2, 1, 4, 2),
 (2, 1, 5, 2),
 (4, 2, 3, 2.5),
 (4, 2, 6, 2.2),
 (6, 2, 2, 3),
 (6, 2, 5, 2.8),
 (7, 3, 1, 2),
 (8, 3, 6, 3),
 (9, 3, 4, 2);

