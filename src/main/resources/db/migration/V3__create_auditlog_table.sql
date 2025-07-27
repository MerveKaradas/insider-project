CREATE TABLE audit_logs (
    audit_log_id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(255) NOT NULL,
    entity_id BIGSERIAL NOT NULL,
    action VARCHAR(255) NOT NULL,
    details TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    performed_by VARCHAR(255) NOT NULL,
    ip_address VARCHAR(255) NOT NULL,
    user_agent VARCHAR(512) NOT NULL
);
