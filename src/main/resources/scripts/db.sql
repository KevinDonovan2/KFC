DROP DATABASE IF EXISTS restoration;
CREATE DATABASE restoration;
\c  restoration;

CREATE TABLE IF NOT EXISTS restaurant (
                                          id SERIAL PRIMARY KEY,
                                          address TEXT
);

CREATE TABLE IF NOT EXISTS menu (
                                    id SERIAL PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL,
    current_price INT NOT NULL
    );

CREATE TABLE sale (
                      id SERIAL PRIMARY KEY,
                      sale_price INT,
                      sale_date TIMESTAMP,
                      quantity INT,
                      restaurant_id INT REFERENCES restaurant(id),
                      menu_id INT REFERENCES menu(id)
);

CREATE TABLE IF NOT EXISTS unit (
                                    id  SERIAL PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS ingredient (
                                          id SERIAL PRIMARY KEY,
                                          price INT NOT NULL,
                                          name VARCHAR(255),
    unit_id INT REFERENCES unit(id)
    );

CREATE TABLE IF NOT EXISTS stock (
                                     id SERIAL PRIMARY KEY,
                                     quantity DOUBLE PRECISION NOT NULL,
                                     restaurant_id INT REFERENCES restaurant(id),
    ingredient_id INT REFERENCES ingredient(id)
    );


CREATE TABLE IF NOT EXISTS ingredient_menu(
                                              id SERIAL PRIMARY KEY,
                                              quantity DOUBLE PRECISION,
                                              menu_id INT REFERENCES menu(id) ,
    ingredient_id INT REFERENCES ingredient(id)
    );


CREATE TABLE IF NOT EXISTS operation_stock (
                                               id SERIAL PRIMARY KEY,
                                               type VARCHAR(50),
    operation_date TIMESTAMP,
    quantity DECIMAL(10, 2),
    ingredient_id INT REFERENCES ingredient(id),
    stock_id INT REFERENCES stock(id)
    );


select SUM(os.quantity)
from operation_stock AS os
         INNER JOIN stock AS s ON os.stock_id = s.id
WHERE os.operation_date BETWEEN '2024-05-27 00:01:49' AND '2024-05-27 01:51:49'
  AND s.restaurant_id = 1;



SELECT i.id   AS ingredientId,
       i.name AS ingredientName,
       m.name AS menuName,
       (select SUM(os.quantity)
        from operation_stock AS os
                 INNER JOIN stock AS s ON os.stock_id = s.id
        WHERE os.type = 'SORTIE'
          AND os.operation_date BETWEEN '2024-05-27 00:01:49' AND '2024-05-27 01:51:49'
          AND s.restaurant_id = 1)
              AS remainingQuantity,
       u.name AS unitName
FROM stock AS s
         INNER JOIN operation_stock AS os ON s.id = os.stock_id
         INNER JOIN ingredient AS i ON os.ingredient_id = i.id
         INNER JOIN ingredient_menu AS im ON im.ingredient_id = i.id
         INNER JOIN menu AS m ON m.id = im.menu_id
         INNER JOIN unit AS u ON u.id = i.unit_id
WHERE os.operation_date BETWEEN '2024-05-27 00:01:49' AND '2024-05-27 01:51:49'
  AND s.restaurant_id = 1 LIMIT  3

SELECT i.id             AS ingredientId,
       i.name           AS ingredientName,
       m.name           AS menuName,
       SUM(os.quantity) AS remainingQuantity,
       u.name           AS unitName
FROM stock AS s
         INNER JOIN operation_stock AS os ON s.id = os.stock_id
         INNER JOIN ingredient AS i ON os.ingredient_id = i.id
         INNER JOIN ingredient_menu AS im ON im.ingredient_id = i.id
         INNER JOIN menu AS m ON m.id = im.menu_id
         INNER JOIN unit AS u ON u.id = i.unit_id
WHERE os.type = 'SORTIE'
  AND s.restaurant_id = 1
  AND os.operation_date BETWEEN '2024-05-27 00:01:49' AND '2024-05-27 01:51:49'
GROUP BY i.id, i.name, m.name, os.operation_date, u.name
ORDER BY os.operation_date DESC LIMIT  3;

select SUM(os.quantity), s.ingredient_id
from stock AS s
         INNER JOIN operation_stock AS os ON os.stock_id = s.id
         INNER JOIN ingredient AS i ON s.ingredient_id = s.id
         INNER JOIN unit AS u ON u.id = i.unit_id
         INNER JOIN ingredient_menu AS im ON im.ingredient_id = i.id
         INNER JOIN menu AS m On im.menu_id = u.id
WHERE os.type = 'SORTIE'
GROUP BY s.ingredient_id;

WITH top_menus AS (
    SELECT sale.menu_id, menu.name,
           SUM(sale.quantity) AS total_quantity
    FROM sale
             INNER JOIN menu ON menu.id = sale.menu_id
    WHERE sale.restaurant_id = 1
      AND sale.sale_date BETWEEN '2024-05-27 00:01:49' AND '2024-05-27 01:51:49'
    GROUP BY sale.menu_id, menu.name
),
     ingredient_usage AS (
         SELECT im.ingredient_id, tm.name,  SUM(im.quantity * tm.total_quantity) AS total_usage
         FROM ingredient_menu im
                  JOIN top_menus tm ON im.menu_id = tm.menu_id
         GROUP BY im.ingredient_id, tm.name
         ORDER BY total_usage DESC
    LIMIT 3
    )
SELECT i.id AS ingredient_id, i.name AS ingredient_name, u.name AS unit_name, iu.name AS menu_name, iu.total_usage
FROM ingredient_usage iu
         JOIN ingredient i ON iu.ingredient_id = i.id
         JOIN unit u ON i.unit_id = u.id;
