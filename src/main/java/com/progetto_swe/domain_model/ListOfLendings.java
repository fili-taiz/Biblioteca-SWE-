package com.progetto_swe.domain_model;

import java.util.ArrayList;
import java.util.Objects;

public class ListOfLendings {
    private ArrayList<Lending> lendings;

    public ListOfLendings(ArrayList<Lending> lendings) {
        this.lendings = lendings;
    }

    public ArrayList<Lending> getLendingsByHirer(Hirer hirer) {
        ArrayList<Lending> hirerLendings = new ArrayList<>();
        for (Lending lending : lendings) {
            if (lending.getHirer().equals(hirer)) {
                hirerLendings.add(lending);
            }
        }
        return hirerLendings;
    }

    public ArrayList<Lending> getLendingsByItem(Item item) {
        ArrayList<Lending> bookLendings = new ArrayList<>();
        for (Lending lending : lendings) {
            if (lending.getItem().equals(item)) {
                bookLendings.add(lending);
            }
        }
        return bookLendings;
    }

    public int getNumberOfLendingsInLibrary(Library library, Item item){
        int n = 0;
        for(Lending l: lendings){
            if(l.getItem().equals(item) && l.getStoragePlace().equals(library)){
                n += 1;
            }
        }
        return n;
    }

    public boolean lendingExist(Hirer hirer, Item item, Library storagePlace) {
        for(Lending l : lendings){
            if(l.getHirer().equals(hirer) && l.getItem().equals(item) && l.getStoragePlace().equals(storagePlace)){
                return true;
            }
        }
        return false;
    }

    public boolean haveLending(Lending lending){
        for(Lending l : lendings){
            if(l.equals(lending)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        ListOfLendings listOfLendings = (ListOfLendings) o;
        return Objects.equals(this.lendings, listOfLendings.getLendings());
    }

    @Override
    public int hashCode() {
        return Objects.hash(lendings);
    }



    public ArrayList<Lending> getLendings(){ return this.lendings; }
}
