package domain_model;
import java.time.LocalDate;

public class Lending {
    private LocalDate lendingDate;
    private Hirer hirer;
    private Item item;
    private Biblioteca storagePlace;

    public Lending() {
        /*Da cancellare */
    }
    
    public Lending(LocalDate lendingDate, Hirer hirer, Item item, Biblioteca storagePlace){
        this.lendingDate = lendingDate;
        this.hirer = hirer;
        this.item = item;
        this.storagePlace = storagePlace;
    }

    public LocalDate getLendingDate(){ return this.lendingDate; }
    public Hirer getHirer(){ return this.hirer; }
    public Item getItem(){ return this.item; }
    public Biblioteca getStoragePlace(){ return storagePlace; }

    public void setLendingDate(LocalDate newLendingDate) { this.lendingDate = newLendingDate; }
    public void setHirer(Hirer newHirer){ this.hirer = newHirer; }
    public void setItem(Item newItem){ this.item = newItem; }



}
