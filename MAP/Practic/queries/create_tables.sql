-- Table: public.dummies

-- DROP TABLE IF EXISTS public.dummies;

CREATE TABLE IF NOT EXISTS public.dummies
(
    id bigint NOT NULL,
    CONSTRAINT dummies_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.dummies
    OWNER to postgres;