CREATE OR REPLACE FUNCTION public.check_reservation_date(
	)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
DELETE FROM reservations WHERE CURRENT_DATE > reservation_date + INTERVAL '7 days'; END;
$BODY$;

CREATE OR REPLACE FUNCTION public.close_to_expiration(
	)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
IF EXISTS(SELECT * FROM lending WHERE lending_date = CURRENT_DATE + INTERVAL '10 days')
THEN PERFORM pg_notify('reservation_expiration', 'Reservation expired!' );
ELSE PERFORM pg_notify('reservation_expiration', 'No expired reservations!' );
END IF; END;
$BODY$;

CREATE OR REPLACE TRIGGER update_available_copies_on_delete
    AFTER DELETE
ON public.reservation
    FOR EACH ROW
    EXECUTE FUNCTION public.increment_number_of_available_copies();

CREATE OR REPLACE TRIGGER update_available_copies_on_insert
    AFTER INSERT
    ON public.reservation
    FOR EACH ROW
    EXECUTE FUNCTION public.decrement_number_of_available_copies();