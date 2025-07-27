CREATE TABLE balances (
    balances_id BIGSERIAL PRIMARY KEY,
    user_id BIGSERIAL NOT NULL UNIQUE,
    balances_amount NUMERIC(19, 2) NOT NULL,
    balances_last_updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT fk_user
        FOREIGN KEY(user_id) REFERENCES users(id)
);
