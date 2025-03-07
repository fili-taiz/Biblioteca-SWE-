package domain_model;
import java.time.LocalDate;

public class Reservation {
    private LocalDate reservationDate;
    private Hirer hirer;
    private Item item;
    private Library storagePlace;

    public Reservation() {
        /*Da cancellare */
    }

    public Reservation(LocalDate reservationDate, Hirer hirer, Item item, Library storagePlace){
        this.reservationDate = reservationDate;
        this.hirer = hirer;
        this.item = item;
        this.storagePlace = storagePlace;
    }

    public LocalDate get_reservation_date(){ return this.reservationDate; }
    public Hirer get_hirer(){ return this.hirer; }
    public Item getItem(){ return this.item; }
    public Library getStoragePlace(){ return storagePlace; }

    public void set_reservation_date(LocalDate newReservationDate){ this.reservationDate = newReservationDate; }
    public void set_hirer(Hirer newHirer){ this.hirer = newHirer; }
    public void setItem(Item newItem){ this.item = newItem; }

}
