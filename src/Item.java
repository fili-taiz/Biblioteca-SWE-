import java.time.LocalDate;
import java.util.ArrayList;
enum Biblioteca{
    BIBLIOTECA_1, BIBLIOTECA_2, BIBLIOTECA_3, BIBLIOTECA_4, BIBLIOTECA_5
}

abstract class Item {
    private String code;
    private String title;
    private LocalDate publicationDate;
    private String language;
    private String category;
    private String link;
    private boolean isBorrowable;
    private ArrayList<Tupla> physicalCopies = new ArrayList<>();
    private ArrayList<Lending> lendings = new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();

    public Item(String code, String title, LocalDate publicationDate, String language, String category, String link, boolean isBorrowable) {
        this.code = code;
        this.title = title;
        this.publicationDate = publicationDate;
        this.language = language;
        this.category = category;
        this.link = link;
        this.isBorrowable = isBorrowable;
    }


    public void addMagazine(Biblioteca library, int numberOfCopies){
        Tupla<Biblioteca, Integer> pc = new Tupla<>(library, numberOfCopies);
        physicalCopies.add(pc);
    }

    public int get_number_of_available_copies(Biblioteca library){
        int n = 0;
        for(Tupla t : physicalCopies){
            if (t.get_first().equals(library)) {
                n = (int)t.get_second() - lendings.size() - reservations.size();
                break;
            }
        }
        return n;
    }

    public void remove_magazine(Biblioteca library){
        physicalCopies.removeIf(t -> t.get_first().equals(library));
    }


    public void add_lending(Lending l){
        lendings.add(l);
    }

    public void add_reservation(Reservation r){
        reservations.add(r);
    }

    public void remove_lending(Lending l){
        lendings.remove(l);
    }

    public void remove_reservation(Reservation r){
        reservations.remove(r);
    }

    public void update_item(Item newItem){
        this.code = newItem.code;
        this.title = newItem.title;
        this.publicationDate = newItem.publicationDate;
        this.language = newItem.language;
        this.category = newItem.category;
        this.link = newItem.link;
        this.isBorrowable = newItem.isBorrowable;
    }

    public void setNumberOfAvailableCopies(Biblioteca library, int newN){
        for (Tupla t : this.getPhysicalCopies()) {
            if (t.get_first().equals(library)) {
                t.set_second(newN);
                break;
            }
        }
    }

    public String getCode(){ return this.code;}
    public String getTitle(){ return this.title;}
    public LocalDate getPublicationDate(){ return this.publicationDate;}
    public String getLanguage(){ return this.language;}
    public String getCategory(){ return this.category;}
    public String getLink(){ return this.link;}
    public boolean getIsBorrowable(){ return this.isBorrowable;}
    public ArrayList<Tupla> getPhysicalCopies(){ return this.physicalCopies; }
    private ArrayList<Lending> getLendings(){ return this.lendings; }
    private ArrayList<Reservation> getReservations(){ return this.reservations; }

    public void setCode(String newCode){ this.code = newCode; }
    public void setTitle(String newTitle){ this.title = newTitle; }
    public void setPublicationDate(LocalDate newPublicationDate){ this.publicationDate = newPublicationDate; }
    public void setLanguage(String newLanguage){ this.language = newLanguage; }
    public void setCategory(String newCategory){ this.category = newCategory;}
    public void setLink(String newLink){ this.link = newLink;}
    public void setIsBorrowable(boolean newIsBorrowable){this.isBorrowable = newIsBorrowable; }

}
