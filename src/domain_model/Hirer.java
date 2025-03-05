package domain_model;
import java.util.ArrayList;

public class Hirer {
    private String userCode;
    private String username;
    private String name;
    private String surname;
    private String eMail;
    private String telephoneNumber;
    private ArrayList<Lending> lendings = new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();
    private CatalogueSearchable searchableCatalogue = new Catalogue();
    
    public Hirer() {
        /*Da cancellare */
    }

    public Hirer(String userCode, String username, String name, String surname, String eMail, String telephoneNumber) {
        this.userCode = userCode;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.eMail = eMail;
        this.telephoneNumber = telephoneNumber;
    }

    public ArrayList<Item> searchItem(String keyWord, String category, boolean dateSort, boolean asc){
        return searchableCatalogue.searchItem(keyWord, category, dateSort, asc);
    }

    public boolean reservePhysicalCopy(Item item, Biblioteca storagePlace){

        return false;
    }

    public String getUserCode() { return userCode; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getEmail() { return eMail; }
    public String getTelephoneNumber() { return telephoneNumber; }
    public ArrayList<Lending> getLendings() { return lendings; }
    public ArrayList<Reservation> getReservations() { return reservations; }
    public void setUserCode(String userCode) { this.userCode = userCode; }
    public void setUsername(String username) { this.username = username; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setEmail(String eMail) { this.eMail = eMail; }
    public void setTelephoneNumber(String telephoneNumber) { this.telephoneNumber = telephoneNumber; }
    public void setLendings(ArrayList<Lending> lendings) { this.lendings = lendings; }
    public void setReservations(ArrayList<Reservation> reservations) { this.reservations = reservations; }

}
