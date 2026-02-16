CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT true,
    deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    sku VARCHAR(50) UNIQUE,
    category_id BIGINT REFERENCES categories(id),
    active BOOLEAN NOT NULL DEFAULT true,
    deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_name ON products(name);

-- Sample data
INSERT INTO categories (name, description) VALUES
('Electronics', 'Electronic devices'),
('Clothing', 'Apparel and fashion'),
('Books', 'Books and publications');

INSERT INTO products (name, description, price, stock_quantity, sku, category_id) VALUES
('iPhone 15', 'Latest Apple smartphone', 999.99, 50, 'SKU-IP15', 1),
('MacBook Pro', 'Professional laptop', 2499.99, 25, 'SKU-MBP', 1),
('T-Shirt', 'Cotton t-shirt', 29.99, 100, 'SKU-TSH', 2),
('Clean Code', 'Software craftsmanship book', 44.99, 75, 'SKU-CC', 3);
