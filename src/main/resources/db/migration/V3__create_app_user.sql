-- V3: user accounts (US1 Create Account)
-- Table is named app_user because "user" is a reserved word in PostgreSQL.
CREATE TABLE app_user (
    id            BIGSERIAL    PRIMARY KEY,
    email         VARCHAR(254) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'STUDENT' CHECK (role IN ('STUDENT', 'MODERATOR')),
    created_at    TIMESTAMP    NOT NULL DEFAULT now()
);

-- Emails are unique regardless of upper/lower case.
CREATE UNIQUE INDEX uq_app_user_email ON app_user (lower(email));
