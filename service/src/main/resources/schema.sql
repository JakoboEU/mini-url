CREATE TABLE IF NOT EXISTS mini_url
(
    mini_uri_code       VARCHAR(36) NOT NULL,
    long_url            VARCHAR(250) NOT NULL,
    created_on          TIMESTAMP(6) DEFAULT NOW() NOT NULL,
    open_count          NUMERIC(4) DEFAULT 0 NOT NULL
);