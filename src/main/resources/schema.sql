CREATE TABLE IF NOT EXISTS t_user
(
    f_id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    f_username   VARCHAR     NOT NULL,
    f_password   VARCHAR     NOT NULL,
    f_phone      VARCHAR     NOT NULL,
    f_avatar     VARCHAR     NOT NULL,
    f_created_at TIMESTAMPTZ NOT NULL,
    f_updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_user_phone ON t_user (f_phone);
