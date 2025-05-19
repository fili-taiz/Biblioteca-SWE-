

CREATE TABLE IF NOT EXISTS public.admin
(
    user_code character varying(128),
    name character varying(32),
    surname character varying(32),
    email character varying(128),
    telephone_number character varying(32),
    working_place character varying(128),
    CONSTRAINT admin_pkey PRIMARY KEY (user_code)
    );

CREATE TABLE IF NOT EXISTS public.hirer
(
    user_code character varying(128),
    name character varying(32),
    surname character varying(32),
    email character varying(128),
    telephone_number character varying(32),
    CONSTRAINT hirer_pkey PRIMARY KEY (user_code)
    );

CREATE TABLE IF NOT EXISTS public.banned_hirers
(
    user_code character varying(128),
    unbanned_date date,
    CONSTRAINT banned_hirers_pkey PRIMARY KEY (user_code),
    CONSTRAINT banned_hirers_user_code_fkey FOREIGN KEY (user_code)
    REFERENCES public.hirer (user_code) MATCH SIMPLE
    );

CREATE TABLE IF NOT EXISTS public.item
(
    code SERIAL NOT NULL,
    title character varying(64),
    publication_date date,
    language character varying(32),
    category character varying(32),
    link character varying(32),
    number_of_pages integer,
    CONSTRAINT item_pkey PRIMARY KEY (code)
    );

CREATE TABLE IF NOT EXISTS public.book
(
    code integer,
    isbn character varying(64),
    publishing_house character varying(128),
    authors character varying(128),
    CONSTRAINT book_pkey PRIMARY KEY (code),
    CONSTRAINT book_code_fkey FOREIGN KEY (code)
    REFERENCES public.item (code) MATCH SIMPLE
    );

CREATE TABLE IF NOT EXISTS public.magazine
(
    code integer,
    publishing_house character varying(64),
    CONSTRAINT magazine_pkey PRIMARY KEY (code),
    CONSTRAINT magazine_code_fkey FOREIGN KEY (code)
    REFERENCES public.item (code) MATCH SIMPLE
    );

CREATE TABLE IF NOT EXISTS public.thesis
(
    code integer,
    author character varying(32),
    supervisors character varying(128),
    university character varying(128),
    CONSTRAINT thesis_pkey PRIMARY KEY (code),
    CONSTRAINT thesis_code_fkey FOREIGN KEY (code)
    REFERENCES public.item (code) MATCH SIMPLE
    );

CREATE TABLE IF NOT EXISTS public.physical_copies
(
    code integer,
    storage_place character varying(64),
    number_of_copies integer,
    borrowable boolean,
    number_of_available_copies integer,
    CONSTRAINT physical_copies_pkey PRIMARY KEY (code, storage_place),
    CONSTRAINT physical_copies_code_fkey FOREIGN KEY (code)
    REFERENCES public.item (code) MATCH SIMPLE,
    CONSTRAINT check_number_of_copies CHECK (number_of_copies > 0),
    CONSTRAINT check_number_of_available_copies CHECK (number_of_available_copies > 0)
    );

CREATE TABLE IF NOT EXISTS public.lending
(
    user_code character varying(128),
    code integer,
    storage_place character varying(64),
    lending_date date,
    maturity_date date,
    CONSTRAINT lending_pkey PRIMARY KEY (user_code, code, storage_place),
    CONSTRAINT lending_code_storage_place_fkey FOREIGN KEY (code, storage_place)
    REFERENCES public.physical_copies (code, storage_place) MATCH SIMPLE,
    CONSTRAINT lending_user_code_fkey FOREIGN KEY (user_code)
    REFERENCES public.hirer (user_code) MATCH SIMPLE
    );

CREATE TABLE IF NOT EXISTS public.reservation
(
    user_code character varying(128),
    code integer,
    storage_place character varying(64),
    reservation_date date,
    CONSTRAINT reservation_pkey PRIMARY KEY (user_code, code, storage_place),
    CONSTRAINT reservation_code_storage_place_fkey FOREIGN KEY (code, storage_place)
    REFERENCES public.physical_copies (code, storage_place) MATCH SIMPLE,
    CONSTRAINT reservation_user_code_fkey FOREIGN KEY (user_code)
    REFERENCES public.hirer (user_code) MATCH SIMPLE
    );

CREATE TABLE IF NOT EXISTS public.user_credentials
(
    user_code character varying(128),
    hashed_password character varying(64),
    salt character varying(32),
    CONSTRAINT user_credentials_pkey PRIMARY KEY (user_code),
    CONSTRAINT user_credentials_user_code_fkey FOREIGN KEY (user_code)
    REFERENCES public.hirer (user_code) MATCH SIMPLE
    );

CREATE TABLE IF NOT EXISTS public.waiting_list
(
    code integer,
    storage_place character varying(64),
    email character varying(128),
    CONSTRAINT waiting_list_pkey PRIMARY KEY (code, storage_place, email),
    CONSTRAINT waiting_list_code_fkey FOREIGN KEY (code)
    REFERENCES public.item (code) MATCH SIMPLE
    );