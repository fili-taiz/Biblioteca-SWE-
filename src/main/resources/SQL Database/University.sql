CREATE TABLE IF NOT EXISTS Library_admin
(
    user_code VARCHAR(32),
    name VARCHAR(32),
    surname VARCHAR(32),
    email VARCHAR(128),
    telephone_number VARCHAR(32),
    working_place VARCHAR(128),
    salt VARCHAR(32),
    hashed_password VARCHAR(64),
    PRIMARY KEY (user_code)
);


CREATE TABLE IF NOT EXISTS University_people
(
user_code VARCHAR(32),
name VARCHAR(32),
surname VARCHAR(32),
email VARCHAR(128),
telephone_number VARCHAR(32),
salt VARCHAR(32),
hashed_password VARCHAR(64),
PRIMARY KEY (user_code)
);
