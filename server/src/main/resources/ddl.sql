CREATE TABLE IF NOT EXISTS categories
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(128) UNIQUE NOT NULL,
    description VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS products
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(128) UNIQUE NOT NULL,
    description VARCHAR(256) NOT NULL,
    producer VARCHAR(256) NOT NULL,
    amount INT NOT NULL,
    price NUMERIC NOT NULL,
    category_id INT REFERENCES categories(id) ON DELETE CASCADE
);