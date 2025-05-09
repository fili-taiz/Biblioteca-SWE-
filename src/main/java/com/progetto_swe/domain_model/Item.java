package com.progetto_swe.domain_model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public abstract class Item {
    protected int code;
    protected int numberOfPages;
    protected String title;
    protected LocalDate publicationDate;
    protected Language language;
    protected Category category;
    protected String link;
    protected HashMap<Library, PhysicalCopies> physicalCopies = new HashMap<>();

    public Item(int code, String title, LocalDate publicationDate, Language language, Category category, String link, int numberOfPages) {
        this.code = code;
        this.title = title;
        this.publicationDate = publicationDate;
        this.language = language;
        this.category = category;
        this.link = link;
        this.numberOfPages = numberOfPages;
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

    public boolean sameField(Item itemCopy){
        if(!itemCopy.getTitle().equals(this.title)){
            return false;
        }
        if(!itemCopy.getPublicationDate().equals(this.publicationDate)){
            return false;
        }
        if(itemCopy.getLanguage() != this.language){
            return false;
        }
        if(itemCopy.getCategory() != this.category){
            return false;
        }
        if (!itemCopy.getLink().equals(this.link)) {
            return false;
        }
        return this.numberOfPages == itemCopy.getNumberOfPages();
    }
    public abstract String[] getValues();

    public abstract ArrayList<String[]> toStringValues();

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        Item item = (Item) o;
        return this.code == item.code && this.title.equals(item.title) && this.publicationDate.equals(item.publicationDate) && this.language.equals(item.language) && this.category.equals(item.category) && this.link.equals(item.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, numberOfPages, title, publicationDate, language, category, link, physicalCopies);
    }


    public boolean isBorrowable(Library library) {
        if(physicalCopies.get(library) == null)
            return false;

        return physicalCopies.get(library).isBorrowable();
    }

    public boolean isBorrowable() {
        for(PhysicalCopies p : physicalCopies.values()){
            if(p.isBorrowable()) return true;
        }
        return false;
    }

    public int getNumberOfLibraries(){
        return physicalCopies.size();
    }

    public int getNumberOfAvailableCopiesInLibrary(ListOfLendings lendings, ListOfReservations reservations, Library library){
        if(physicalCopies.get(library) == null){
            return -1;
        }
        return physicalCopies.get(library).getNumberOfPhysicalCopies() - lendings.getNumberOfLendingsInLibrary(library, this) - reservations.getNumberOfReservationsInLibrary(library, this);
    }

    public int getCode(){ return this.code;}
    public String getTitle(){ return this.title;}
    public LocalDate getPublicationDate(){ return this.publicationDate;}
    public Language getLanguage(){ return this.language;}
    public Category getCategory(){ return this.category;}
    public String getLink(){ return this.link;}
    public int getNumberOfPages() { return this.numberOfPages; }

    public PhysicalCopies getLibraryPhysicalCopies(Library library) {
        if(physicalCopies.get(library) == null){
            return null;
        }
        return physicalCopies.get(library);
    }

    public HashMap<Library, PhysicalCopies> getPhysicalCopies() {
        return physicalCopies;
    }

    public void setCode(int newCode){ this.code = newCode; }
    public void setCategory(Category newCategory){ this.category = newCategory;}
    public void setPhysicalCopies(HashMap<Library, PhysicalCopies> physicalCopies) { this.physicalCopies = physicalCopies; }

}
