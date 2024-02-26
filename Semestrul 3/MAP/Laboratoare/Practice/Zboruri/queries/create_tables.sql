CREATE DATABASE zboruri
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- Table: public.clients

-- DROP TABLE IF EXISTS public.clients;

CREATE TABLE IF NOT EXISTS public.clients
(
    username character varying(100) COLLATE pg_catalog."default" NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT clients_pkey PRIMARY KEY (username)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.clients
    OWNER to postgres;
	
insert into clients(username, name) values
	('gig102', 'Gigel Marian'),
	('ddan_000', 'Dan Danescu'),
	('bogdanbogdan', 'Bogdan Andreescu'),
	('mihct0209', 'Mihai Constantinescu'),
	('Mmqwerty', 'Andrei Banescu'),
	('d4v1d', 'David Marinescu');
	
-- Table: public.flights

-- DROP TABLE IF EXISTS public.flights;

CREATE TABLE IF NOT EXISTS public.flights
(
    "flightId" bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    "from" character varying(100) COLLATE pg_catalog."default" NOT NULL,
    "to" character varying(100) COLLATE pg_catalog."default" NOT NULL,
    "departureTime" timestamp without time zone NOT NULL,
    "landingTime" timestamp without time zone NOT NULL,
    seats integer NOT NULL,
    CONSTRAINT flights_pkey PRIMARY KEY ("flightId")
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.flights
    OWNER to postgres;
	
-- Table: public.tickets

-- DROP TABLE IF EXISTS public.tickets;

CREATE TABLE IF NOT EXISTS public.tickets
(
    username character varying(100) COLLATE pg_catalog."default" NOT NULL,
    "flightId" bigint NOT NULL,
    "purchaseTime" timestamp without time zone NOT NULL,
    CONSTRAINT tickets_pkey PRIMARY KEY (username, "flightId"),
    CONSTRAINT "fk_flightId" FOREIGN KEY ("flightId")
        REFERENCES public.flights ("flightId") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_username FOREIGN KEY (username)
        REFERENCES public.clients (username) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.tickets
    OWNER to postgres;