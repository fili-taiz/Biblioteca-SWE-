import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap<Biblioteca, Integer> physicalCopies = new HashMap<>();
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
        physicalCopies.put(library, numberOfCopies);
    }

    public int getNumberOfLend(Biblioteca library, Item item){
        int n = 0;
        for(Lending l: lendings){
            if(l.getStoragePlace().equals(library) && l.getItem() == item){
                n += 1;
            }
        }
        return n;
    }

    public int getNumberOfRes(Biblioteca library, Item item){
        int n = 0;
        for(Reservation r: reservations){
            if(r.getStoragePlace().equals(library) && r.getItem() == item){
                n += 1;
            }
        }
        return n;
    }

    public int getNumberOfAvailableCopies(Biblioteca library, Item item){
        return physicalCopies.get(library) - getNumberOfLend(library, item) - getNumberOfRes(library, item);
    }

    public void removeMagazine(Biblioteca library){
        physicalCopies.remove(library);
    }


    public void addLending(Lending l){
        lendings.add(l);
    }

    public void addReservation(Reservation r){
        reservations.add(r);
    }

    public void removeLending(Lending l){
        lendings.remove(l);
    }

    public void removeReservation(Reservation r){
        reservations.remove(r);
    }

    public void updateItem(Item newItem){
        this.code = newItem.code;
        this.title = newItem.title;
        this.publicationDate = newItem.publicationDate;
        this.language = newItem.language;
        this.category = newItem.category;
        this.link = newItem.link;
        this.isBorrowable = newItem.isBorrowable;
    }

    public void setNumberOfCopies(Biblioteca library, int newN){
        for(Biblioteca b : physicalCopies.keySet()){
            if(library == b){
                physicalCopies.replace(b, newN);
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
    public HashMap<Biblioteca, Integer> getPhysicalCopies(){ return this.physicalCopies; }
    public ArrayList<Lending> getLendings(){ return this.lendings; }
    public ArrayList<Reservation> getReservations(){ return this.reservations; }

    public void setCode(String newCode){ this.code = newCode; }
    public void setTitle(String newTitle){ this.title = newTitle; }
    public void setPublicationDate(LocalDate newPublicationDate){ this.publicationDate = newPublicationDate; }
    public void setLanguage(String newLanguage){ this.language = newLanguage; }
    public void setCategory(String newCategory){ this.category = newCategory;}
    public void setLink(String newLink){ this.link = newLink;}
    public void setIsBorrowable(boolean newIsBorrowable){ this.isBorrowable = newIsBorrowable; }
    public void setLendings(ArrayList<Lending> newLendings){ this.lendings = newLendings; }
    public void setReservations(ArrayList<Reservation> newReservations){ this.reservations = newReservations; }

}
