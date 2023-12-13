CREATE SCHEMA sevfruit;

-- Null display is "<null>".
\pset null '<null>'

--  
SET search_path TO sevfruit;

SHOW search_path;

-- Поставщики
CREATE TABLE SUPPLIER
(
	id int NOT NULL PRIMARY KEY, -- SERIAL
	name varchar(50) NOT NULL UNIQUE
);

-- Продукция
CREATE TABLE PRODUCT
(
	id int NOT NULL PRIMARY KEY,
	name varchar(50) NOT NULL UNIQUE
);

-- Периоды
CREATE TABLE PERIOD
(
	id int NOT NULL PRIMARY KEY,
	name varchar(50) NOT NULL UNIQUE
);

-- Прайс-лист
CREATE TABLE PRICE
(
	id int NOT NULL PRIMARY KEY,
	supplier_id int NOT NULL REFERENCES SUPPLIER(id),
	period_id int NOT NULL REFERENCES PERIOD(id)
);

-- Позиция в прайс листе
CREATE TABLE PRICE_PRODUCT
(
	price_id int NOT NULL REFERENCES PRICE(id),
	product_id int NOT NULL REFERENCES PRODUCT(id),
	value real NOT NULL, -- Цена за тонну 
	PRIMARY KEY (price_id, product_id)	
);

-- Поставки
CREATE TABLE SHIPMENT
(
	id int NOT NULL PRIMARY KEY,
	price_id int NOT NULL REFERENCES PRICE(id),
	UNIQUE (id, price_id)
);

CREATE TABLE SHIPMENT_PRODUCT
(
	shipment_id int NOT NULL,
	price_id int NOT NULL,
	product_id int NOT NULL,
	quantity real NOT NULL, -- кол-во в тоннах
	PRIMARY KEY (shipment_id, price_id, product_id),
	FOREIGN KEY (shipment_id, price_id) REFERENCES SHIPMENT (id, price_id),
	FOREIGN KEY (price_id, product_id) REFERENCES PRICE_PRODUCT (price_id, product_id)
);


-- DROP SCHEMA sevfruit CASCADE;
