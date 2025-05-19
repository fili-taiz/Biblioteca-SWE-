CREATE TABLE IF NOT EXISTS public.library_admin
(
    user_code character varying(32),
    name character varying(32),
    surname character varying(32),
    email character varying(128),
    telephone_number character varying(32),
    working_place character varying(128),
    salt character varying(32),
    hashed_password character varying(64),
    CONSTRAINT library_admin_pkey PRIMARY KEY (user_code),
    )


CREATE TABLE IF NOT EXISTS public.university_people
(
    user_code character varying(32),
    name character varying(32),
    surname character varying(32),
    email character varying(128),
    telephone_number character varying(32),
    salt character varying(32),
    hashed_password character varying(64),
    CONSTRAINT university_people_pkey PRIMARY KEY (user_code),
    )