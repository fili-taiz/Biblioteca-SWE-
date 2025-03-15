package com.progetto_swe.domain_model;
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

    public Item getItem(int code) {
        return items.get(code);
    }


    public boolean addItem(Item item){
        return items.add(item);
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

}
