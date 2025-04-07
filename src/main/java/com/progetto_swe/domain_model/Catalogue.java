package com.progetto_swe.domain_model;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Catalogue{
    ArrayList<Item> items;


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

    public ArrayList<Item> advanceSearchItem(String keywords, Category category, Language language, boolean borrowable, LocalDate startDate, LocalDate endDate) {
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

    public ArrayList<Item> getItems() { return items; }
}
