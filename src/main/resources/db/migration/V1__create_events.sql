CREATE TABLE events (
    id BIGINT NOT NULL AUTO_INCREMENT,

    title VARCHAR(255) NOT NULL,

    description TEXT,

    category VARCHAR(50) NOT NULL,

    venue VARCHAR(255) NOT NULL,

    start_time TIMESTAMP NOT NULL,

    organizer VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
);