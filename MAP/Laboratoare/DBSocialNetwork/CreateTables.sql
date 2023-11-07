-- CREATE DATABASE socialnetwork
--     WITH
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     CONNECTION LIMIT = -1
--     IS_TEMPLATE = False;

DROP TABLE IF EXISTS friendships;  
DROP TABLE IF EXISTS users;  

CREATE TABLE public.users
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    first_name character varying,
    last_name character varying,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

CREATE TABLE friendships (
	id1 BIGINT,
	id2 BIGINT CHECK (id1 < id2),
	friends_from timestamp WITHOUT time zone,
	CONSTRAINT fk_id1
      FOREIGN KEY(id1) 
	  REFERENCES users(id),
	CONSTRAINT fk_id2
      FOREIGN KEY(id2) 
	  REFERENCES users(id),
	PRIMARY KEY (id1, id2)
);

