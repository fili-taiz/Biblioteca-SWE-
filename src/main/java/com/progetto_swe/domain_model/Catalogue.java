package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Catalogue{
    private ArrayList<Item> items;


    public Catalogue(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Item> searchItem(String keywords, Category category) {
        String[] splittedKeyword = keywords.split(" ");
        ArrayList<Item> result = new ArrayList<>();
        for (Item i : this.items) {
            for (String keyword : splittedKeyword){
                if (i.getCategory().equals(category) && i.contains(keyword)) {
                    result.add(i);
                }
            }
        }
        return result;
    }

    public ArrayList<Item> advancedSearchItem(String keywords, Category category, Language language, boolean borrowable, LocalDate startDate, LocalDate endDate) {
        String[] splittedKeyword = keywords.split(" ");
        ArrayList<Item> result = new ArrayList<>();
        for (Item i : this.items) {
            for (String keyword : splittedKeyword){
                if (i.getCategory().equals(category) && i.getLanguage().equals(language) && i.isBorrowable() == borrowable &&
                        i.getPublicationDate().isAfter(startDate) && i.getPublicationDate().isBefore(endDate) && i.contains(keyword)) {
                    result.add(i);
                }
            }
        }
        return result;
    }

    public int contains(Item itemsCopy) {
        for(Item item : items){
            if(item.sameField(itemsCopy)){
                return item.getCode();
            }
        }
        return -1;
    }

    public Item getItem(int code) {
        for(Item item : items){
            if(item.getCode() == code){
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }

        Catalogue catalogue = (Catalogue) o;

        return Objects.equals(this.items, catalogue.items);

    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }



    public ArrayList<Item> getItems(){ return this.items; }

}
