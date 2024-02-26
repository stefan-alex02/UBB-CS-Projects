-- Table: public.locations

-- DROP TABLE IF EXISTS public.locations;

CREATE TABLE IF NOT EXISTS public.locations
(
    location_id double precision NOT NULL,
    location_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT locations_pkey PRIMARY KEY (location_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.locations
    OWNER to postgres;

-- Table: public.hotels

-- DROP TABLE IF EXISTS public.hotels;

CREATE TABLE IF NOT EXISTS public.hotels
(
    hotel_id double precision NOT NULL,
    location_id double precision NOT NULL,
    hotel_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    no_rooms integer NOT NULL,
    price_per_night double precision NOT NULL,
    type character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT hotels_pkey PRIMARY KEY (hotel_id),
    CONSTRAINT fk_location_id FOREIGN KEY (location_id)
        REFERENCES public.locations (location_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.hotels
    OWNER to postgres;
	
-- Table: public.special_offers

-- DROP TABLE IF EXISTS public.special_offers;

CREATE TABLE IF NOT EXISTS public.special_offers
(
    special_offer_id double precision NOT NULL,
    hotel_id double precision NOT NULL,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone NOT NULL,
    percents integer NOT NULL,
    CONSTRAINT special_offers_pkey PRIMARY KEY (special_offer_id),
    CONSTRAINT fk_hotel_id FOREIGN KEY (hotel_id)
        REFERENCES public.hotels (hotel_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT ck_percents CHECK (percents >= 1 AND percents <= 100)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.special_offers
    OWNER to postgres;
	
-- Table: public.clients

-- DROP TABLE IF EXISTS public.clients;

CREATE TABLE IF NOT EXISTS public.clients
(
    client_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    fidelity_grade integer NOT NULL,
    age integer NOT NULL,
    hobbies character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT clients_pkey PRIMARY KEY (client_id),
    CONSTRAINT ck_fidelity_grade CHECK (fidelity_grade >= 1 AND fidelity_grade <= 100)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.clients
    OWNER to postgres;
	
-- Table: public.reservations

-- DROP TABLE IF EXISTS public.reservations;

CREATE TABLE IF NOT EXISTS public.reservations
(
    reservation_id double precision NOT NULL,
    client_id bigint NOT NULL,
    hotel_id double precision NOT NULL,
    start_date timestamp without time zone NOT NULL,
    no_nights integer NOT NULL,
    CONSTRAINT reservations_pkey PRIMARY KEY (reservation_id),
    CONSTRAINT fk_client_id FOREIGN KEY (client_id)
        REFERENCES public.clients (client_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_hotel_id FOREIGN KEY (hotel_id)
        REFERENCES public.hotels (hotel_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.reservations
    OWNER to postgres;