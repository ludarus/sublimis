--
-- PostgreSQL database cluster dump
--

\restrict ev7nw0ozMh8OmCTEtHr9Nm7ecWInfGhxuAO6GxilrZv0UzGIahvwFU6yubLGi0K

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Roles
--

CREATE ROLE postgres;
ALTER ROLE postgres WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION BYPASSRLS;
CREATE ROLE sublimis;
ALTER ROLE sublimis WITH NOSUPERUSER INHERIT NOCREATEROLE NOCREATEDB LOGIN NOREPLICATION NOBYPASSRLS PASSWORD 'SCRAM-SHA-256$4096:A6EyCtJrB6JGF6/SnIY5PQ==$VwT0BmaeGIDTFeQtu38osaLKvZvFS4uR83Q06zb1OnM=:xeOPa8U0LT2MCuOwrL09odyC5drAV4c7/3RlyPlMUBM=';

--
-- User Configurations
--








\unrestrict ev7nw0ozMh8OmCTEtHr9Nm7ecWInfGhxuAO6GxilrZv0UzGIahvwFU6yubLGi0K

--
-- PostgreSQL database cluster dump complete
--

