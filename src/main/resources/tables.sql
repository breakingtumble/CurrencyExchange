CREATE TABLE exchange_rates (
    id INTEGER PRIMARY KEY NOT NULL,
    base_currency_id INTEGER NOT NULL REFERENCES  currencies(id),
    target_currency_id INTEGER NOT NULL REFERENCES currencies(id),
    rate NUMERIC NOT NULL
);
CREATE UNIQUE INDEX i1 ON exchange_rates(base_currency_id, target_currency_id);

SELECT e.id, e.rate, c.code as base_currency_code, c1.code as target_currency_code FROM exchange_rates e
INNER JOIN currencies c on e.base_currency_id = c.id INNER JOIN currencies c1 ON e.target_currency_id = c1.id;

INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (1, 3, 36.9);

SELECT e.id, e.rate,
       c1.id AS base_currency_id, c1.code AS base_currency_code, c1.fullname AS base_currency_name, c1.sign as base_currency_sign,
       c2.id AS target_currency_id, c2.code AS target_currency_code, c2.fullname AS target_currency_name, c2.sign as target_currency_sign
FROM exchange_rates e INNER JOIN currencies c1 on c1.id = e.base_currency_id INNER JOIN currencies c2 on c2.id = e.target_currency_id;