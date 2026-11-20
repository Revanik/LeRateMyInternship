-- V2: internship reviews (US2 Create Review, US3 Rate Internship, US4 Edit Review)
CREATE TABLE review (
    id             BIGSERIAL PRIMARY KEY,
    company_name   VARCHAR(200) NOT NULL,
    position_title VARCHAR(200) NOT NULL,
    title          VARCHAR(150) NOT NULL,
    body           TEXT         NOT NULL,
    overall_rating INT          NOT NULL CHECK (overall_rating BETWEEN 1 AND 5),
    created_at     TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP    NOT NULL DEFAULT now()
);