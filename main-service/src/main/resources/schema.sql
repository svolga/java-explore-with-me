CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL,
    CONSTRAINT uq_email UNIQUE (email),
    CONSTRAINT pk_users PRIMARY KEY (user_id)

    );

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    category_name VARCHAR(255) NOT NULL,
    CONSTRAINT uq_name UNIQUE (category_name),
    CONSTRAINT pk_categories PRIMARY KEY (category_id)
    );

CREATE TABLE IF NOT EXISTS locations
(
    location_id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (location_id)
    );

CREATE TABLE IF NOT EXISTS events
(
    event_id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title              VARCHAR(120)                            NOT NULL,
    state              VARCHAR(30),
    views              INT,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT                                  NOT NULL
    REFERENCES categories (category_id) ON DELETE CASCADE,
    confirmed_requests INT DEFAULT 0,
    created_on         TIMESTAMP                               NOT NULL,
    description        VARCHAR(7000),
    event_date         TIMESTAMP                               NOT NULL,
    initiator_id       BIGINT                                  NOT NULL
    REFERENCES users (user_id) ON DELETE CASCADE,
    location_id        BIGINT
    REFERENCES locations (location_id) ON DELETE CASCADE,
    paid               BOOLEAN,
    participant_limit  INT,
    published_on       TIMESTAMP,
    request_moderation BOOLEAN DEFAULT TRUE,
    CONSTRAINT pk_events PRIMARY KEY (event_id)
    );

CREATE TABLE IF NOT EXISTS participation_requests
(
    request_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    status     VARCHAR(32) NOT NULL,
    created    TIMESTAMP NOT NULL,
    event_id   BIGINT NOT NULL
    REFERENCES events (event_id) ON DELETE CASCADE,
    requester_id BIGINT NOT NULL
    REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT pk_requests PRIMARY KEY (request_id)
);


CREATE TABLE IF NOT EXISTS compilations (
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title   VARCHAR(50) NOT NULL,
    pinned  BOOLEAN,
    CONSTRAINT pk_compilations PRIMARY KEY (compilation_id)
);

CREATE TABLE IF NOT EXISTS compilations_events (
    event_id BIGINT NOT NULL
    REFERENCES events (event_id) ON DELETE CASCADE,
    compilation_id INT NOT NULL
    REFERENCES compilations (compilation_id) ON DELETE CASCADE,
    CONSTRAINT pk_compilations_events PRIMARY KEY (compilation_id, event_id)
);
