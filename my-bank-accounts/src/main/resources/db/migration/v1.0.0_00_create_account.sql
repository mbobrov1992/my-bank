CREATE TABLE IF NOT EXISTS account
(
    id         UUID PRIMARY KEY,
    first_name TEXT,
    last_name  TEXT,
    birth_date DATE,
    balance    NUMERIC(12, 2) NOT NULL DEFAULT 0 CHECK (balance >= 0),
    created_at TIMESTAMP      NOT NULL DEFAULT now(),
    updated_at TIMESTAMP      NOT NULL DEFAULT now()
);