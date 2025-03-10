package domain_model;
import java.util.ArrayList;

public class Hirer extends SoftwareUser{
    ArrayList<Lending> lendings = new ArrayList<>();
    ArrayList<Reservation> reservations = new ArrayList<>();
    Catalogue searchableCatalogue = new Catalogue();

    public Hirer(String user_code, String username, String name, String surname, String e_mail, String telephone_number, UserProfile user_profile) {
        super(user_code, username, name, surname, e_mail, telephone_number, user_profile);
    }

    public ArrayList<Item> searchItem(String keyword, Category category){
        return searchableCatalogue.searchItem(keyword, category);
    }

    public boolean reservePhysicalCopy(Item item, Library storagePlace){

        return false;
    }

    public ArrayList<Lending> getLendings() { return lendings; }
    public ArrayList<Reservation> getReservations() { return reservations; }

    public void setLendings(ArrayList<Lending> lendings) { this.lendings = lendings; }
    public void setReservations(ArrayList<Reservation> reservations) { this.reservations = reservations; }

}
