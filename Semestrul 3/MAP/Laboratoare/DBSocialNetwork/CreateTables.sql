-- CREATE DATABASE socialnetwork
--     WITH
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     CONNECTION LIMIT = -1
--     IS_TEMPLATE = False;

DROP TABLE IF EXISTS public.friend_requests;
DROP TABLE IF EXISTS public.reply_messages;
DROP TABLE IF EXISTS public.messages_receivers;
DROP TABLE IF EXISTS public.messages;
DROP TABLE IF EXISTS public.friendships;
DROP TABLE IF EXISTS public.users;

-- Table: public.users

CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    username character varying NOT NULL,
	first_name character varying COLLATE pg_catalog."default",
    last_name character varying COLLATE pg_catalog."default",
	password character varying NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT username_unique UNIQUE (username)
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
	
	
	
CREATE TABLE public.messages
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
	CONSTRAINT pk_id PRIMARY KEY (id),
    "from_user_id" bigint NOT NULL,
    message character varying NOT NULL,
    date timestamp without time zone NOT NULL,
    CONSTRAINT "fk_from_user_id" FOREIGN KEY ("from_user_id")
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.messages
    OWNER to postgres;
	
	
	
CREATE TABLE public.messages_receivers
(
    message_id bigint NOT NULL,
    receiver_id bigint NOT NULL,
    CONSTRAINT pk_message_receiver PRIMARY KEY (message_id, receiver_id),
    CONSTRAINT fk_message_id FOREIGN KEY (message_id)
        REFERENCES public.messages (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_receiver_id FOREIGN KEY (receiver_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.messages_receivers
    OWNER to postgres;



CREATE TABLE public.reply_messages
(
    message_id bigint NOT NULL,
    reply_to_id bigint NOT NULL,
    PRIMARY KEY (message_id),
    CONSTRAINT fk_message_id FOREIGN KEY (message_id)
        REFERENCES public.messages (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_reply_to_id FOREIGN KEY (reply_to_id)
        REFERENCES public.messages (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT ch_reply_and_message_different CHECK (message_id <> reply_to_id)
);

ALTER TABLE IF EXISTS public.reply_messages
    OWNER to postgres;
	
	
CREATE TABLE public.friend_requests
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    from_id bigint NOT NULL,
    to_id bigint NOT NULL,
	status character varying NOT NULL DEFAULT 'PENDING',
    CONSTRAINT ch_status CHECK (status = 'PENDING' OR status = 'APPROVED' OR status = 'REJECTED'),
    date timestamp without time zone NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_from_id FOREIGN KEY (from_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_to_id FOREIGN KEY (to_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.friend_requests
    OWNER to postgres;