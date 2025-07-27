CREATE TABLE transactions (
    transactions_id BIGSERIAL PRIMARY KEY,
    from_user_id BIGINT NOT NULL,
    to_user_id BIGINT NOT NULL,
    transaction_amount NUMERIC(19, 2) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    transaction_created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_from_user FOREIGN KEY (from_user_id) REFERENCES users(id),
    CONSTRAINT fk_to_user FOREIGN KEY (to_user_id) REFERENCES users(id)
);
