package com.progetto_swe.domain_model;

import java.util.ArrayList;

public class ListOfLendings {
    ArrayList<Lending> lendings = new ArrayList<>();

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

    public ArrayList<Lending> getLendingsByItem(Item item) {
        ArrayList<Lending> bookLendings = new ArrayList<>();
        for (Lending lending : lendings) {
            if (lending.getItem().equals(item)) {
                bookLendings.add(lending);
            }
        }
        return bookLendings;
    }
}
