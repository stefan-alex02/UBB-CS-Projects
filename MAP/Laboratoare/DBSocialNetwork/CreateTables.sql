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

CREATE TABLE public.friendships
(
    id1 bigint NOT NULL,
    id2 bigint NOT NULL,
    friends_from timestamp without time zone NOT NULL,
    CONSTRAINT pk_idpair PRIMARY KEY (id1, id2),
    CONSTRAINT fk_id1 FOREIGN KEY (id1)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT fk_id2 FOREIGN KEY (id2)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT ascending_pair CHECK (id1 < id2)
);

ALTER TABLE IF EXISTS public.friendships
    OWNER to postgres;

