CREATE TABLE product (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         price NUMERIC(10,2) NOT NULL,
                         creation_datetime TIMESTAMP NOT NULL
);

CREATE TABLE product_category (
                                  id SERIAL PRIMARY KEY,
                                  name VARCHAR(255) NOT NULL,
                                  product_id INT NOT NULL REFERENCES product(id) ON DELETE CASCADE
);

-- Privilèges pour notre utilisateur
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO product_manager_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO product_manager_user;