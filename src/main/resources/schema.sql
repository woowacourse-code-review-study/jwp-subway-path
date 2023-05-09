CREATE TABLE IF NOT EXISTS station
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS line
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    name  VARCHAR(255)          NOT NULL UNIQUE,
    color VARCHAR(20)           NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS section
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    line_id        BIGINT                NOT NULL,
    origin_id      BIGINT                NOT NULL,
    destination_id BIGINT                NOT NULL,
    distance       INT                   NOT NULL,
    PRIMARY KEY (id)
);
