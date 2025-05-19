CREATE TABLE IF NOT EXISTS public.library_admin
(
    user_code character varying(32) COLLATE pg_catalog."default" NOT NULL,
    name character varying(32) COLLATE pg_catalog."default",
    surname character varying(32) COLLATE pg_catalog."default",
    email character varying(128) COLLATE pg_catalog."default",
    telephone_number character varying(32) COLLATE pg_catalog."default",
    working_place character varying(128) COLLATE pg_catalog."default",
    salt character varying(32) COLLATE pg_catalog."default",
    hashed_password character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT library_admin_pkey PRIMARY KEY (user_code),
    CONSTRAINT library_admin_email_key UNIQUE (email),
    CONSTRAINT library_admin_telephone_number_key UNIQUE (telephone_number)
    )


CREATE TABLE IF NOT EXISTS public.university_people
(
    user_code character varying(32) COLLATE pg_catalog."default" NOT NULL,
    name character varying(32) COLLATE pg_catalog."default",
    surname character varying(32) COLLATE pg_catalog."default",
    email character varying(128) COLLATE pg_catalog."default",
    telephone_number character varying(32) COLLATE pg_catalog."default",
    salt character varying(32) COLLATE pg_catalog."default",
    hashed_password character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT university_people_pkey PRIMARY KEY (user_code),
    CONSTRAINT university_people_email_key UNIQUE (email),
    CONSTRAINT university_people_telephone_number_key UNIQUE (telephone_number)
    )