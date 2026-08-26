CREATE TABLE spots (
    spot_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    latitude NUMERIC(9, 6) NOT NULL,
    longitude NUMERIC(9, 6) NOT NULL,
    chain VARCHAR(100),
    map_link VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE reports (
    report_id BIGSERIAL PRIMARY KEY,
    reporter_id UUID NOT NULL,
    spot_id BIGINT NOT NULL REFERENCES spots(spot_id) ON DELETE CASCADE,
    outlets VARCHAR(10),
    CONSTRAINT chk_outlets CHECK (outlets IN ('NONE', 'FEW', 'MANY')),
    noise VARCHAR(10),
    CONSTRAINT chk_noise CHECK (noise IN ('QUIET', 'MODERATE', 'LOUD')),
    has_wifi BOOLEAN,
    hidden BOOLEAN NOT NULL DEFAULT FALSE,
    additional_comments TEXT,
    CONSTRAINT chk_comment_length CHECK (LENGTH(additional_comments) <= 500),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_not_empty CHECK (
        outlets IS NOT NULL OR noise IS NOT NULL OR has_wifi IS NOT NULL
    )
);

CREATE INDEX idx_reports_spot_created ON reports (spot_id, created_at DESC);

CREATE INDEX idx_reports_reporter_created ON reports (reporter_id, created_at DESC);