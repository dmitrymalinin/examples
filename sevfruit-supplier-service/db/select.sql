SELECT
 S.name as supp, P.name as prod, PERIOD.name as period, PP.value, SP.quantity
FROM
 SHIPMENT_PRODUCT SP
 LEFT OUTER JOIN PRICE_PRODUCT PP ON SP.price_id = PP.price_id AND SP.product_id = PP.product_id
 LEFT OUTER JOIN PRICE ON PP.price_id = PRICE.id
 LEFT OUTER JOIN PERIOD ON PRICE.period_id = PERIOD.id
 LEFT OUTER JOIN SUPPLIER S ON PRICE.supplier_id = S.id
 LEFT OUTER JOIN PRODUCT P ON PP.product_id = P.id


SELECT
 S.name as supp, P.name as prod, sum(PP.value), sum(SP.quantity)
FROM
 SHIPMENT_PRODUCT SP
 LEFT OUTER JOIN PRICE_PRODUCT PP ON SP.price_id = PP.price_id AND SP.product_id = PP.product_id
 LEFT OUTER JOIN PRICE ON PP.price_id = PRICE.id
 LEFT OUTER JOIN PERIOD ON PRICE.period_id = PERIOD.id
 LEFT OUTER JOIN SUPPLIER S ON PRICE.supplier_id = S.id
 LEFT OUTER JOIN PRODUCT P ON PP.product_id = P.id
GROUP BY S.id, P.id
ORDER BY S.id 