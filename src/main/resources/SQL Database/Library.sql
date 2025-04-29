CREATE TABLE IF NOT EXISTS Item(
code SERIAL,
title VARCHAR(64),
publication_date DATE,
language VARCHAR(32),
category VARCHAR(32),
link VARCHAR(32),
number_of_pages INTEGER,
PRIMARY KEY(code)
);

CREATE TABLE IF NOT EXISTS Magazine(
code INTEGER,
publishing_house VARCHAR(64),
PRIMARY KEY(code),
FOREIGN KEY (code) REFERENCES Item (code)
);

CREATE TABLE IF NOT EXISTS Thesis(
code INTEGER,
author VARCHAR(32),
supervisors VARCHAR(128),
university VARCHAR(128),
PRIMARY KEY(code),
FOREIGN KEY (code) REFERENCES Item (code)
);

CREATE TABLE IF NOT EXISTS Book(
code INTEGER,
isbn VARCHAR (64),
publishing_house VARCHAR(128),
authors VARCHAR(128),
PRIMARY KEY(code),
FOREIGN KEY (code) REFERENCES Item (code)
);

CREATE TABLE IF NOT EXISTS Physical_Copies(
code INTEGER,
storage_place VARCHAR(64),
number_of_copies INTEGER,
borrowable BOOLEAN,
PRIMARY KEY(code, storage_place),
FOREIGN KEY (code) REFERENCES Item (code)
);

CREATE TABLE IF NOT EXISTS Hirer(
user_code VARCHAR(32),
name VARCHAR(32),
surname VARCHAR(32),
email VARCHAR(128) UNIQUE,
telephone_number VARCHAR(32) UNIQUE,
PRIMARY KEY(user_code)
);

CREATE TABLE IF NOT EXISTS Admin(
user_code VARCHAR(32),
name VARCHAR(32),
surname VARCHAR(32),
email VARCHAR(128) UNIQUE,
telephone_number VARCHAR(32) UNIQUE,
working_place VARCHAR(128),
PRIMARY KEY(user_code)
);

CREATE TABLE IF NOT EXISTS User_credentials(
user_code VARCHAR(32),
hashed_password VARCHAR(64),
salt VARCHAR(32),
PRIMARY KEY(user_code),
FOREIGN KEY (user_code) REFERENCES Hirer(user_code),
FOREIGN KEY (user_code) REFERENCES Admin(user_code)
);

CREATE TABLE IF NOT EXISTS Lending(
user_code VARCHAR(32),
code INTEGER,
storage_place VARCHAR(64),
lending_date DATE,
PRIMARY KEY(user_code, code, storage_place),
FOREIGN KEY (user_code) REFERENCES Hirer(user_code),
FOREIGN KEY (code, storage_place) REFERENCES Physical_Copies(code, storage_place)
);

CREATE TABLE IF NOT EXISTS Reservation(
user_code VARCHAR(32),
code INTEGER,
storage_place VARCHAR(64),
reservation_date DATE,
PRIMARY KEY(user_code, code, storage_place),
FOREIGN KEY (user_code) REFERENCES Hirer(user_code),
FOREIGN KEY (code, storage_place) REFERENCES Physical_Copies(code, storage_place)
);

CREATE TABLE IF NOT EXISTS Banned_Hirers(
user_code VARCHAR(32),
unbanned_date DATE,
PRIMARY KEY(user_code),
FOREIGN KEY (user_code) REFERENCES Hirer(user_code)
);