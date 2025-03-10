package domain_model;
import java.time.LocalDate;

public class Lending {
    LocalDate lendingDate;
    Hirer hirer;
    Item item;
    Library storagePlace;

    
    public Lending(LocalDate lendingDate, Hirer hirer, Item item, Library storagePlace){
        this.lendingDate = lendingDate;
        this.hirer = hirer;
        this.item = item;
        this.storagePlace = storagePlace;
    }

    public Hirer getHirer(){ return this.hirer; }
    public Item getItem(){ return this.item; }
    public Library getStoragePlace(){ return storagePlace; }

    public void setHirer(Hirer newHirer){ this.hirer = newHirer; }
    public void setItem(Item newItem){ this.item = newItem; }



}
