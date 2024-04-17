CREATE TABLE IF NOT EXISTS books (
    id SERIAL PRIMARY KEY,
    author VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    publishing_year INT,
    category VARCHAR(255),
    available BOOLEAN DEFAULT TRUE
);