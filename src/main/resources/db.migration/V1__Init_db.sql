CREATE TABLE IF NOT EXISTS `USERS`
(
    ID       BIGINT AUTO_INCREMENT NOT NULL,
    USERNAME VARCHAR(255)          NOT NULL,
    PASSWORD VARCHAR(255)          NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (ID)
);

INSERT INTO USERS (USERNAME, PASSWORD)
VALUES ('admin', '$2a$12$nTSvSSYQEjjn15TIN5ppbuJS.FU7e/Yz.aE42E479K6sdlaqbNEdO')
    ON DUPLICATE KEY UPDATE USERNAME = VALUES(USERS.USERNAME),
    PASSWORD = VALUES(USERS.PASSWORD);

CREATE TABLE IF NOT EXISTS `DOMAINS`
(
    ID     BIGINT AUTO_INCREMENT NOT NULL,
    DOMAIN VARCHAR(253)          NOT NULL UNIQUE,
    CONSTRAINT domains_pk PRIMARY KEY (ID)
);