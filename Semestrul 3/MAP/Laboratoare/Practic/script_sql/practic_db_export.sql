--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0
-- Dumped by pg_dump version 16.0

-- Started on 2024-01-23 11:45:21

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 41797)
-- Name: cities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cities (
    id character varying(100) NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.cities OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 41825)
-- Name: tickets; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tickets (
    id bigint NOT NULL,
    train_id character varying(100) NOT NULL,
    departure_city_id character varying(100) NOT NULL,
    date date NOT NULL
);


ALTER TABLE public.tickets OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 41824)
-- Name: tickets_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.tickets ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.tickets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 216 (class 1259 OID 41805)
-- Name: train_stations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.train_stations (
    train_id character varying(100) NOT NULL,
    departure_city_id character varying(100) NOT NULL,
    destination_city_id character varying(100) NOT NULL
);


ALTER TABLE public.train_stations OWNER TO postgres;

--
-- TOC entry 4848 (class 0 OID 41797)
-- Dependencies: 215
-- Data for Name: cities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cities (id, name) FROM stdin;
C1	Cluj-Napoca
C2	Sibiu
C3	Brasov
C4	Oradea
C5	Targu Mures
\.


--
-- TOC entry 4851 (class 0 OID 41825)
-- Dependencies: 218
-- Data for Name: tickets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tickets (id, train_id, departure_city_id, date) FROM stdin;
1	T1	C4	2024-01-25
2	T1	C4	2024-02-02
3	T1	C4	2024-01-25
4	T1	C1	2024-01-25
5	T1	C4	2024-01-25
6	T2	C4	2024-01-25
\.


--
-- TOC entry 4849 (class 0 OID 41805)
-- Dependencies: 216
-- Data for Name: train_stations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.train_stations (train_id, departure_city_id, destination_city_id) FROM stdin;
T1	C4	C1
T1	C1	C2
T1	C2	C3
T2	C4	C1
T2	C1	C5
T2	C5	C3
T2	C3	C2
\.


--
-- TOC entry 4857 (class 0 OID 0)
-- Dependencies: 217
-- Name: tickets_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tickets_id_seq', 6, true);


--
-- TOC entry 4697 (class 2606 OID 41811)
-- Name: cities cities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT cities_pkey PRIMARY KEY (id);


--
-- TOC entry 4699 (class 2606 OID 41813)
-- Name: train_stations pk_train_station; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.train_stations
    ADD CONSTRAINT pk_train_station PRIMARY KEY (train_id, departure_city_id, destination_city_id);


--
-- TOC entry 4701 (class 2606 OID 41829)
-- Name: tickets tickets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_pkey PRIMARY KEY (id);


--
-- TOC entry 4702 (class 2606 OID 41814)
-- Name: train_stations fk_departure_city_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.train_stations
    ADD CONSTRAINT fk_departure_city_id FOREIGN KEY (departure_city_id) REFERENCES public.cities(id);


--
-- TOC entry 4704 (class 2606 OID 41830)
-- Name: tickets fk_departure_city_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT fk_departure_city_id FOREIGN KEY (departure_city_id) REFERENCES public.cities(id);


--
-- TOC entry 4703 (class 2606 OID 41819)
-- Name: train_stations fk_destination_city_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.train_stations
    ADD CONSTRAINT fk_destination_city_id FOREIGN KEY (destination_city_id) REFERENCES public.cities(id);


-- Completed on 2024-01-23 11:45:21

--
-- PostgreSQL database dump complete
--

