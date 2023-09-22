CREATE TABLE IF NOT EXISTS notes
(
    id      SERIAL PRIMARY KEY,
    title   VARCHAR(255)             NOT NULL,
    content TEXT                     NOT NULL,
    date    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT                   NOT NULL
);