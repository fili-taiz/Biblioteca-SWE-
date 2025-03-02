import java.time.LocalDate;

public class Lending {
    LocalDate lendingDate;
    Hirer hirer;
    Item item;
    Biblioteca storagePlace;

    public Lending(LocalDate lendingDate, Hirer hirer, Item item, Biblioteca storagePlace){
        this.lendingDate = lendingDate;
        this.hirer = hirer;
        this.item = item;
        this.storagePlace = storagePlace;
    }

    public LocalDate get_lending_date(){ return this.lendingDate; }
    public Hirer get_hirer(){ return this.hirer; }
    public Item getItem(){ return this.item; }
    public Biblioteca getStoragePlace(){ return storagePlace; }

    public void set_lending_date(LocalDate newLendingDate) { this.lendingDate = lendingDate; }
    public void set_hirer(Hirer newHirer){ this.hirer = hirer; }
    public void setItem(Item newItem){ this.item = newItem; }



}
