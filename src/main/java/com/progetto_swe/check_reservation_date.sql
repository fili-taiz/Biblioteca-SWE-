CREATE OR REPLACE FUNCTION check_reservation_date() RETURNS void AS $$
BEGIN
DELETE FROM reservations WHERE CURRENT_DATE > reservation_date + INTERVAL '7 days'; END; $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION close_to_expiration() RETURNS void AS $$
BEGIN
IF EXISTS(SELECT * FROM lending WHERE lending_date = CURRENT_DATE + INTERVAL '10 days') 
THEN PERFORM pg_notify('reservation_expiration', 'Reservation expired!' );
ELSE PERFORM pg_notify('reservation_expiration', 'No expired reservations!' );
END IF; END; $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION unban_hirers() RETURNS void AS $$
BEGIN
DELETE FROM banned_hirers WHERE CURRENT_DATE = unbanned_date; END; $$ LANGUAGE plpgsql;




