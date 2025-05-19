CREATE TABLE IF NOT EXISTS public.admin
(
    user_code character varying(128) COLLATE pg_catalog."default" NOT NULL,
    name character varying(32) COLLATE pg_catalog."default",
    surname character varying(32) COLLATE pg_catalog."default",
    email character varying(128) COLLATE pg_catalog."default",
    telephone_number character varying(32) COLLATE pg_catalog."default",
    working_place character varying(128) COLLATE pg_catalog."default",
    CONSTRAINT admin_pkey PRIMARY KEY (user_code)
    )

CREATE TABLE IF NOT EXISTS public.hirer
(
    user_code character varying(128) COLLATE pg_catalog."default" NOT NULL,
    name character varying(32) COLLATE pg_catalog."default",
    surname character varying(32) COLLATE pg_catalog."default",
    email character varying(128) COLLATE pg_catalog."default",
    telephone_number character varying(32) COLLATE pg_catalog."default",
    CONSTRAINT hirer_pkey PRIMARY KEY (user_code)
    )

CREATE TABLE IF NOT EXISTS public.banned_hirers
(
    user_code character varying(128) COLLATE pg_catalog."default" NOT NULL,
    unbanned_date date,
    CONSTRAINT banned_hirers_pkey PRIMARY KEY (user_code),
    CONSTRAINT banned_hirers_user_code_fkey FOREIGN KEY (user_code)
    REFERENCES public.hirer (user_code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

CREATE TABLE IF NOT EXISTS public.item
(
    code integer NOT NULL DEFAULT nextval('item_code_seq'::regclass),
    title character varying(64) COLLATE pg_catalog."default",
    publication_date date,
    language character varying(32) COLLATE pg_catalog."default",
    category character varying(32) COLLATE pg_catalog."default",
    link character varying(32) COLLATE pg_catalog."default",
    number_of_pages integer,
    CONSTRAINT item_pkey PRIMARY KEY (code)
    )

CREATE TABLE IF NOT EXISTS public.book
(
    code integer NOT NULL,
    isbn character varying(64) COLLATE pg_catalog."default",
    publishing_house character varying(128) COLLATE pg_catalog."default",
    authors character varying(128) COLLATE pg_catalog."default",
    CONSTRAINT book_pkey PRIMARY KEY (code),
    CONSTRAINT book_code_fkey FOREIGN KEY (code)
    REFERENCES public.item (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

CREATE TABLE IF NOT EXISTS public.magazine
(
    code integer NOT NULL,
    publishing_house character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT magazine_pkey PRIMARY KEY (code),
    CONSTRAINT magazine_code_fkey FOREIGN KEY (code)
    REFERENCES public.item (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

CREATE TABLE IF NOT EXISTS public.thesis
(
    code integer NOT NULL,
    author character varying(32) COLLATE pg_catalog."default",
    supervisors character varying(128) COLLATE pg_catalog."default",
    university character varying(128) COLLATE pg_catalog."default",
    CONSTRAINT thesis_pkey PRIMARY KEY (code),
    CONSTRAINT thesis_code_fkey FOREIGN KEY (code)
    REFERENCES public.item (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

CREATE TABLE IF NOT EXISTS public.physical_copies
(
    code integer NOT NULL,
    storage_place character varying(64) COLLATE pg_catalog."default" NOT NULL,
    number_of_copies integer,
    borrowable boolean,
    number_of_available_copies integer,
    CONSTRAINT physical_copies_pkey PRIMARY KEY (code, storage_place),
    CONSTRAINT physical_copies_code_fkey FOREIGN KEY (code)
    REFERENCES public.item (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT check_number_of_copies CHECK (number_of_copies > 0),
    CONSTRAINT check_number_of_available_copies CHECK (number_of_available_copies > 0)
    )

CREATE TABLE IF NOT EXISTS public.lending
(
    user_code character varying(128) COLLATE pg_catalog."default" NOT NULL,
    code integer NOT NULL,
    storage_place character varying(64) COLLATE pg_catalog."default" NOT NULL,
    lending_date date,
    maturity_date date,
    CONSTRAINT lending_pkey PRIMARY KEY (user_code, code, storage_place),
    CONSTRAINT lending_code_storage_place_fkey FOREIGN KEY (code, storage_place)
    REFERENCES public.physical_copies (code, storage_place) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT lending_user_code_fkey FOREIGN KEY (user_code)
    REFERENCES public.hirer (user_code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

CREATE TABLE IF NOT EXISTS public.reservation
(
    user_code character varying(128) COLLATE pg_catalog."default" NOT NULL,
    code integer NOT NULL,
    storage_place character varying(64) COLLATE pg_catalog."default" NOT NULL,
    reservation_date date,
    CONSTRAINT reservation_pkey PRIMARY KEY (user_code, code, storage_place),
    CONSTRAINT reservation_code_storage_place_fkey FOREIGN KEY (code, storage_place)
    REFERENCES public.physical_copies (code, storage_place) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT reservation_user_code_fkey FOREIGN KEY (user_code)
    REFERENCES public.hirer (user_code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

CREATE TABLE IF NOT EXISTS public.user_credentials
(
    user_code character varying(128) COLLATE pg_catalog."default" NOT NULL,
    hashed_password character varying(64) COLLATE pg_catalog."default",
    salt character varying(32) COLLATE pg_catalog."default",
    CONSTRAINT user_credentials_pkey PRIMARY KEY (user_code),
    CONSTRAINT user_credentials_user_code_fkey FOREIGN KEY (user_code)
    REFERENCES public.hirer (user_code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

CREATE TABLE IF NOT EXISTS public.waiting_list
(
    code integer NOT NULL,
    storage_place character varying(64) COLLATE pg_catalog."default" NOT NULL,
    email character varying(128) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT waiting_list_pkey PRIMARY KEY (code, storage_place, email),
    CONSTRAINT waiting_list_code_fkey FOREIGN KEY (code)
    REFERENCES public.item (code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )