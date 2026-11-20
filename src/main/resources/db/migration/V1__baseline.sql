-- Baseline so Flyway has something to run. Real tables (users, company, internship, review, ...) come in later migrations.
CREATE TABLE app_info (
    id      SERIAL PRIMARY KEY,
    created TIMESTAMP NOT NULL DEFAULT now()
);
