

-- Table: public.persons

-- DROP TABLE IF EXISTS public.persons;

CREATE TABLE IF NOT EXISTS public.persons
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    username character varying(100) COLLATE pg_catalog."default" NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT persons_pkey PRIMARY KEY (id),
    CONSTRAINT uq_username UNIQUE (username)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.persons
    OWNER to postgres;
	

-- Table: public.drivers

-- DROP TABLE IF EXISTS public.drivers;

CREATE TABLE IF NOT EXISTS public.drivers
(
    person_id bigint NOT NULL,
    indicativ_masina character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT drivers_pkey PRIMARY KEY (person_id),
    CONSTRAINT fk_person_id FOREIGN KEY (person_id)
        REFERENCES public.persons (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.drivers
    OWNER to postgres;
	

-- Table: public.orders

-- DROP TABLE IF EXISTS public.orders;

CREATE TABLE IF NOT EXISTS public.orders
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    person_id bigint NOT NULL,
    driver_id bigint NOT NULL,
    date timestamp without time zone NOT NULL,
    CONSTRAINT orders_pkey PRIMARY KEY (id),
    CONSTRAINT fk_driver_id FOREIGN KEY (driver_id)
        REFERENCES public.drivers (person_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_person_id FOREIGN KEY (person_id)
        REFERENCES public.persons (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.orders
    OWNER to postgres;
	
delete from public.persons;

insert into public.persons(username, name) values
	('gig102', 'Gigel Marian'),
	('ddan_000', 'Dan Danescu'),
	('bogdanbogdan', 'Bogdan Andreescu'),
	('mihct0209', 'Mihai Constantinescu'),
	('Mmqwerty', 'Andrei Banescu'),
	('d4v1d', 'David Marinescu');
	
select * from public.persons;

insert into drivers(person_id, indicativ_masina) values
	(5, 'CJ-10-DDX'),
	(7, 'MM-33-MHC'),
	(9, 'CJ-02-DAV');
	
select * from drivers;
	
	