--ALTER DATABASE socialnetwork SET SINGLE_USER WITH ROLLBACK IMMEDIATE;

CREATE DATABASE socialnetwork
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

DROP TABLE users;

CREATE TABLE users (
	id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	first_name CHARACTER VARYING(50),
	last_name CHARACTER VARYING(50)
);

INSERT INTO users(first_name, last_name) VALUES
	('Ion', 'Popescu'),
	('Cosmin', 'Andrei');

SELECT *
FROM users