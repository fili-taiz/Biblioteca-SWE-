package com.progetto_swe.domain_model;
import java.time.LocalDate;
import java.util.Objects;

public class Lending {
    private LocalDate lendingDate;
    private Hirer hirer;
    private Item item;
    private Library storagePlace;


    public Lending(LocalDate lendingDate, Hirer hirer, Item item, Library storagePlace){
        this.lendingDate = lendingDate;
        this.hirer = hirer;
        this.item = item;
        this.storagePlace = storagePlace;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        Lending lending = (Lending) o;
        return (this.lendingDate.equals(lending.lendingDate)) && (this.hirer.equals(lending.hirer)) && (this.item.equals(lending.item)) && (this.storagePlace.equals(lending.storagePlace));
    }

    @Override
    public int hashCode() {
        return Objects.hash(lendingDate, hirer, item, storagePlace);
    }


    public Hirer getHirer(){ return this.hirer; }
    public Item getItem(){ return this.item; }
    public Library getStoragePlace(){ return this.storagePlace; }
    public LocalDate getLendingDate() { return this.lendingDate; }

    public void setHirer(Hirer newHirer){ this.hirer = newHirer; }
    public void setItem(Item newItem){ this.item = newItem; }



}
