CREATE TABLE IF NOT EXISTS University_People(
user_code VARCHAR(32),
name VARCHAR(32),
surname VARCHAR(32),
email VARCHAR(128) UNIQUE,
telephone_number VARCHAR(32) UNIQUE,
salt VARCHAR(32),
hashed_password VARCHAR(64),
PRIMARY KEY(user_code)
);

CREATE TABLE IF NOT EXISTS Library_Admin(
user_code VARCHAR(32),
name VARCHAR(32),
surname VARCHAR(32),
email VARCHAR(128) UNIQUE,
telephone_number VARCHAR(32) UNIQUE,
working_place VARCHAR(128),
salt VARCHAR(32),
hashed_password VARCHAR(64),
PRIMARY KEY(user_code)
);