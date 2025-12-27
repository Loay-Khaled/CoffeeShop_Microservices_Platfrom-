-- Loyalty accounts table
CREATE TABLE loyalty_accounts (
    id BIGSERIAL PRIMARY KEY,
    customer_username VARCHAR(255) NOT NULL UNIQUE,
    points_balance INT DEFAULT 0,
    tier VARCHAR(50) DEFAULT 'BRONZE',
    lifetime_points INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Loyalty transactions table
CREATE TABLE loyalty_transactions (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES loyalty_accounts(id),
    order_id BIGINT,
    points INT NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index for faster lookups
CREATE INDEX idx_loyalty_transactions_account ON loyalty_transactions(account_id);
CREATE INDEX idx_loyalty_accounts_username ON loyalty_accounts(customer_username);
