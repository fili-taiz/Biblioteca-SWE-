package domain_model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
public abstract class Item {
    private String code;
    private String title;
    private LocalDate publicationDate;
    private Language language;
    private Category category;
    private String link;
    private boolean isBorrowable;
    private HashMap<Library, Integer> physicalCopies = new HashMap<>();
    private ArrayList<Lending> lendings = new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();

    public Item(String code, String title, LocalDate publicationDate, Language language, Category category, String link, boolean isBorrowable) {
        this.code = code;
        this.title = title;
        this.publicationDate = publicationDate;
        this.language = language;
        this.category = category;
        this.link = link;
        this.isBorrowable = isBorrowable;
    }

    public Item(){}

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        Item item = (Item) o;
        return this.code.equals(item.code) && this.title.equals(item.title) && this.publicationDate.equals(item.publicationDate) && this.language.equals(item.language) && this.category.equals(item.category) && this.link.equals(item.link) && this.isBorrowable == item.isBorrowable;
    }


    public void gettt(){
        System.out.println(getClass());
    }

    public void addMagazine(Library library, int numberOfCopies){
        physicalCopies.put(library, numberOfCopies);
    }

    public int getNumberOfLend(Library library, Item item){
        int n = 0;
        for(Lending l: lendings){
            if(l.getStoragePlace().equals(library) && l.getItem() == item){
                n += 1;
            }
        }
        return n;
    }

    public int getNumberOfRes(Library library, Item item){
        int n = 0;
        for(Reservation r: reservations){
            if(r.getStoragePlace().equals(library) && r.getItem() == item){
                n += 1;
            }
        }
        return n;
    }

    public int getNumberOfAvailableCopies(Library library, Item item){
        return physicalCopies.get(library) - getNumberOfLend(library, item) - getNumberOfRes(library, item);
    }

    public void removeMagazine(Library library){
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

    public boolean updateItem(Item newItem){
        this.code = newItem.code;
        this.title = newItem.title;
        this.publicationDate = newItem.publicationDate;
        this.language = newItem.language;
        this.category = newItem.category;
        this.link = newItem.link;
        this.isBorrowable = newItem.isBorrowable;
        return true;
    }

    public void setNumberOfCopies(Library library, int newN){
        for(Library b : physicalCopies.keySet()){
            if(library == b){
                physicalCopies.replace(b, newN);
            }
        }
    }

    public String getCode(){ return this.code;}
    public String getTitle(){ return this.title;}
    public LocalDate getPublicationDate(){ return this.publicationDate;}
    public Language getLanguage(){ return this.language;}
    public Category getCategory(){ return this.category;}
    public String getLink(){ return this.link;}
    public boolean getIsBorrowable(){ return this.isBorrowable;}
    public HashMap<Library, Integer> getPhysicalCopies(){ return this.physicalCopies; }
    public ArrayList<Lending> getLendings(){ return this.lendings; }
    public ArrayList<Reservation> getReservations(){ return this.reservations; }

    public void setCode(String newCode){ this.code = newCode; }
    public void setTitle(String newTitle){ this.title = newTitle; }
    public void setPublicationDate(LocalDate newPublicationDate){ this.publicationDate = newPublicationDate; }
    public void setLanguage(Language newLanguage){ this.language = newLanguage; }
    public void setCategory(Category newCategory){ this.category = newCategory;}
    public void setLink(String newLink){ this.link = newLink;}
    public void setIsBorrowable(boolean newIsBorrowable){ this.isBorrowable = newIsBorrowable; }
    public void setLendings(ArrayList<Lending> newLendings){ this.lendings = newLendings; }
    public void setReservations(ArrayList<Reservation> newReservations){ this.reservations = newReservations; }

}
