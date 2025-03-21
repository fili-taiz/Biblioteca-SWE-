package com.progetto_swe.domain_model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
public class Item {
    int code;
    String title;
    LocalDate publicationDate;
    Language language;
    Category category;
    String link;
    boolean borrowable;
    HashMap<Library, Integer> physicalCopies = new HashMap<>();
    ArrayList<Lending> lendings = new ArrayList<>();
    ArrayList<Reservation> reservations = new ArrayList<>();

    public Item(int code, String title, LocalDate publicationDate, Language language, Category category, String link, boolean borrowable) {
        this.code = code;
        this.title = title;
        this.publicationDate = publicationDate;
        this.language = language;
        this.category = category;
        this.link = link;
        this.borrowable = borrowable;
    }

    public boolean contains(String keyword){
        if(title.toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        if(publicationDate.toString().contains(keyword.toUpperCase())){
            return true;
        }
        return language.name().toUpperCase().contains(keyword.toUpperCase());
    }

    public boolean sameField(Item itemsCopy){
        if(!itemsCopy.getTitle().equals(this.title)){
            return false;
        }
        if(!itemsCopy.getPublicationDate().equals(this.publicationDate)){
            return false;
        }
        if(itemsCopy.getLanguage() != this.language){
            return false;
        }
        if(itemsCopy.getCategory() != this.category){
            return false;
        }
        if (!itemsCopy.getLink().equals(this.link)) {
            return false;
        }
        if(itemsCopy.isBorrowable() != this.borrowable){
            return false;
        }
        return true;
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
        return this.code == item.code && this.title.equals(item.title) && this.publicationDate.equals(item.publicationDate) && this.language.equals(item.language) && this.category.equals(item.category) && this.link.equals(item.link) && this.borrowable == item.borrowable;
    }

    //messo perchè equals da solo dava warning, e cercando su stack overflow ogni talvolta che overrido equal dovrei 
    //farlo anche con hashCode ma ora non ho voglia di capire come funziona di preciso, ho solo capito che viene invocato 
    //dalle hashmap e hashset per metterli come chiave di una tupla, se sono uguali metterli nello stesso pair se no metterlo in una posizione diversa;
    @Override
    public int hashCode () {
        return super.hashCode();
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

    public boolean addLending(Lending lending){
        lendings.add(lending);
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

    public boolean addCopies(Library library, int numberOfCopies){
        if(!physicalCopies.containsKey(library)){
            physicalCopies.put(library, numberOfCopies);
            return true;
        }
        physicalCopies.put(library, physicalCopies.get(library)+numberOfCopies);
        return true;
    }

    public boolean setCopies(Library library, int numberOfCopies){
        if(!physicalCopies.containsKey(library)){
            return false;
        }
        physicalCopies.put(library, numberOfCopies);
        return true;
    }

    public boolean removeCopies(Library library){
        return physicalCopies.remove(library) >= 0;
    }

    public int getCode(){ return this.code;}
    public String getTitle(){ return this.title;}
    public LocalDate getPublicationDate(){ return this.publicationDate;}
    public Language getLanguage(){ return this.language;}
    public Category getCategory(){ return this.category;}
    public String getLink(){ return this.link;}
    public boolean isBorrowable(){ return this.borrowable;}

    public void setCode(int newCode){ this.code = newCode; }
    public void setTitle(String newTitle){ this.title = newTitle; }
    public void setPublicationDate(LocalDate newPublicationDate){ this.publicationDate = newPublicationDate; }
    public void setLanguage(Language newLanguage){ this.language = newLanguage; }
    public void setCategory(Category newCategory){ this.category = newCategory;}
    public void setLink(String newLink){ this.link = newLink;}


}
