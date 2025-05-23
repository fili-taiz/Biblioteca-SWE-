CREATE OR REPLACE FUNCTION increment_number_of_available_copies()
RETURNS TRIGGER AS $$
BEGIN
UPDATE physical_copies
SET number_of_available_copies = number_of_available_copies + 1
WHERE itemCode = OLD.itemCode AND storage_place = OLD.storage_place;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION decrement_number_of_available_copies()
RETURNS TRIGGER AS $$
BEGIN
UPDATE physical_copies
SET number_of_available_copies = number_of_available_copies - 1
WHERE itemCode = NEW.itemCode AND storage_place = NEW.storage_place;
END;
$$ LANGUAGE plpgsql;

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