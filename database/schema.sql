--
-- PostgreSQL database dump
--

\restrict a27C6CyAVens6z3vZ48jbGScxE8uiguZJ3QRgM9anhYumKVV4ysqwCCBQqnKxdX

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
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
-- Name: googleusers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.googleusers (
    id text NOT NULL,
    email character varying(254),
    name character varying(70),
    familyname character varying(70),
    givenname character varying(70),
    email_verified boolean,
    locale character varying(500),
    img text
);


ALTER TABLE public.googleusers OWNER TO postgres;

--
-- Name: sessions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sessions (
    userid text,
    sessionid uuid,
    created timestamp without time zone,
    expires timestamp without time zone
);


ALTER TABLE public.sessions OWNER TO postgres;

--
-- Name: googleusers googleusers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.googleusers
    ADD CONSTRAINT googleusers_pkey PRIMARY KEY (id);


--
-- Name: sessions unique_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sessions
    ADD CONSTRAINT unique_id UNIQUE (userid);


--
-- Name: sessions unique_session; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sessions
    ADD CONSTRAINT unique_session UNIQUE (sessionid);


--
-- Name: sessions sessions_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sessions
    ADD CONSTRAINT sessions_userid_fkey FOREIGN KEY (userid) REFERENCES public.googleusers(id);


--
-- Name: TABLE googleusers; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.googleusers TO sublimis;


--
-- Name: TABLE sessions; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.sessions TO sublimis;


--
-- PostgreSQL database dump complete
--

\unrestrict a27C6CyAVens6z3vZ48jbGScxE8uiguZJ3QRgM9anhYumKVV4ysqwCCBQqnKxdX

