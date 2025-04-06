package com.progetto_swe.domain_model;
import java.sql.Statement;
import java.util.ArrayList;

public class Catalogue{
    ArrayList<Item> items;

    public Catalogue() {
        this.items = new ArrayList<>();
    }

    public Catalogue(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Item> searchItem(String keyWords, Category category) {
        String[] splittedKeyWord = keyWords.split(" ");
        ArrayList<Item> result = new ArrayList<>();
        for (Item i : this.items) {
            for (String keyWord : splittedKeyWord){
                if (i.getCategory().equals(category) && i.contains(keyWord)) {
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
    }

   /* public boolean removeCopies(int itemCode, Library storagePlace){
        getItem(itemCode).removeCopies(storagePlace);
        return false;
    }*/
}
