package domain_model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
public abstract class Item {
    String code;
    String title;
    LocalDate publicationDate;
    Language language;
    Category category;
    String link;
    boolean borrowable;
    HashMap<Library, Integer> physicalCopies = new HashMap<>();
    ArrayList<Lending> lendings = new ArrayList<>();
    ArrayList<Reservation> reservations = new ArrayList<>();

    public Item(String code, String title, LocalDate publicationDate, Language language, Category category, String link, boolean borrowable) {
        this.code = code;
        this.title = title;
        this.publicationDate = publicationDate;
        this.language = language;
        this.category = category;
        this.link = link;
        this.borrowable = borrowable;
    }



    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        Item item = (Item) o;
        return this.code.equals(item.code) && this.title.equals(item.title) && this.publicationDate.equals(item.publicationDate) && this.language.equals(item.language) && this.category.equals(item.category) && this.link.equals(item.link) && this.borrowable == item.borrowable;
    }


    public int getNumberOfLendings(Library library, Item item){
        if(item instanceof Thesis){
            return 0;
        }
        int n = 0;
        for(Lending l: lendings){
            if(l.getStoragePlace().equals(library) && l.getItem() == item){
                n += 1;
            }
        }
        return n;
    }

    public int getNumberOfReservations(Library library, Item item){
        if(item instanceof Thesis){
            return 0;
        }
        int n = 0;
        for(Reservation r: reservations){
            if(r.getStoragePlace().equals(library) && r.getItem() == item){
                n += 1;
            }
        }
        return n;
    }

    public int getNumberOfAvailableCopies(Library library, Item item){

        return physicalCopies.get(library) - getNumberOfLendings(library, item) - getNumberOfReservations(library, item);
    }

    public boolean addLending(Lending l){
        if(getNumberOfAvailableCopies(l.getStoragePlace(), l.getItem()) == 0){
            return false;
        }
        lendings.add(l);
        return true;
    }

    public boolean addReservation(Reservation r){
        if(getNumberOfAvailableCopies(r.getStoragePlace(), r.getItem()) == 0){
            return false;
        }
        reservations.add(r);
        return true;
    }

    public boolean removeLending(Lending l){
        return lendings.remove(l);
    }

    public boolean removeReservation(Reservation r){
        return reservations.remove(r);
    }

    public boolean updateItem(Item new_item){
        this.code = new_item.code;
        this.title = new_item.title;
        this.publicationDate = new_item.publicationDate;
        this.language = new_item.language;
        this.category = new_item.category;
        this.link = new_item.link;
        this.borrowable = new_item.borrowable;
        return true;
    }


    public String getCode(){ return this.code;}
    public String getTitle(){ return this.title;}
    public LocalDate getPublicationDate(){ return this.publicationDate;}
    public Language getLanguage(){ return this.language;}
    public Category getCategory(){ return this.category;}
    public String getLink(){ return this.link;}
    public boolean isBorrowable(){ return this.borrowable;}

    public void setCode(String newCode){ this.code = newCode; }
    public void setTitle(String newTitle){ this.title = newTitle; }
    public void setPublicationDate(LocalDate newPublicationDate){ this.publicationDate = newPublicationDate; }
    public void setLanguage(Language newLanguage){ this.language = newLanguage; }
    public void setCategory(Category newCategory){ this.category = newCategory;}
    public void setLink(String newLink){ this.link = newLink;}


}
