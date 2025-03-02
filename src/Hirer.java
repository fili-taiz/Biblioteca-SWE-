import java.util.ArrayList;

public class Hirer {
    String userCode;
    String username;
    String name;
    String surname;
    String eMail;
    String telephoneNumber;
    ArrayList<Lending> lendings = new ArrayList<>();
    ArrayList<Reservation> reservations = new ArrayList<>();
    CatalogueSearchable cs = new Catalogue();

    public Hirer(String userCode, String username, String name, String surname, String eMail, String telephoneNumber) {
        this.userCode = userCode;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.eMail = eMail;
        this.telephoneNumber = telephoneNumber;
    }

    public void lend_item(Item item, Biblioteca library){
        if(item instanceof Magazine){
            Magazine magazine = (Magazine) item;
            if(magazine.get_number_of_available_copies(library) > 0){

            }
        }

    }
}
