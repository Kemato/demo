CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(100) NOT NULL,
                       status VARCHAR(50) NOT NULL,
                       author BIGINT NOT NULL REFERENCES users(id),
                       priority VARCHAR(20),
                       deadline TIMESTAMP,
                       assigned BIGINT REFERENCES users(id),
                       date_updated TIMESTAMP,
                       description TEXT,
                       date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       date_finished TIMESTAMP
);