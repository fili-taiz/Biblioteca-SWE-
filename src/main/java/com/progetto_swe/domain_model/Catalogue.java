package com.progetto_swe.domain_model;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class Catalogue{
    ArrayList<Item> items;

    public Catalogue(ArrayList<Item> items) {
        this.items = Objects.requireNonNullElseGet(items, ArrayList::new);
    } //se items è non nullo mette this.items = items, altrimenti lo crea, l'ho fatto per togliere l'altro costruttore

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
            if(item.getCode()==code){
                return item;
            }
        }
        return null;
    }


    public boolean addItem(Item item){
        return items.add(item);
    } //non lo testo perché è semplicemente un add

    public ArrayList<Item> getItems(){ return this.items; }

   /* public boolean removeCopies(int itemCode, Library storagePlace){
        getItem(itemCode).removeCopies(storagePlace);
        return false;
    }*/
}
