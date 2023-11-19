-- CREATE DATABASE socialnetwork
--     WITH
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     CONNECTION LIMIT = -1
--     IS_TEMPLATE = False;

DROP TABLE IF EXISTS public.friendships;
DROP TABLE IF EXISTS public.users;

-- Table: public.users

CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    first_name character varying COLLATE pg_catalog."default",
    last_name character varying COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;
	
	
	
-- Table: public.friendships

CREATE TABLE IF NOT EXISTS public.friendships
(
    id1 bigint NOT NULL,
    id2 bigint NOT NULL,
    friends_from timestamp without time zone NOT NULL,
    CONSTRAINT pk_idpair PRIMARY KEY (id1, id2),
    CONSTRAINT fk_id1 FOREIGN KEY (id1)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_id2 FOREIGN KEY (id2)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT ascending_pair CHECK (id1 < id2)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.friendships
    OWNER to postgres;
