CREATE TABLE IF NOT EXISTS cash_advance_notifications (
    id BIGSERIAL PRIMARY KEY,
    cash_advance_approval_id BIGINT NULL,
    recipient_id BIGINT NULL,
    sender_id BIGINT NULL,
    type VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    notification_status VARCHAR(50),
    created_by BIGINT,
    last_modified_by BIGINT,
    version INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_cash_advance_notifications_recipient
    ON cash_advance_notifications (recipient_id);
