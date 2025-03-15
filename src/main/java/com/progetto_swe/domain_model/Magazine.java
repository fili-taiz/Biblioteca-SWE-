package com.progetto_swe.domain_model;

import java.time.LocalDate;

public class Magazine extends Item {

    String publishingHouse;

    public Magazine(int code, String title, LocalDate publicationDate, Language language, Category category, String link, boolean borrowable,
             String publishingHouse) {
        super(code, title, publicationDate, language, category, link, borrowable);
        this.publishingHouse = publishingHouse;
    }

    public String getPublishingHouse() {
        return this.publishingHouse;
    }

    public void setPublishingHouse(String newPublishingHouse) {
        this.publishingHouse = newPublishingHouse;
    }

    @Override
    public boolean updateItem(Item newItem) {
        if (newItem instanceof Magazine magazine) {
            super.updateItem(newItem);
            this.setPublishingHouse(magazine.getPublishingHouse());
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(String keyword){
        if(publishingHouse.toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        return super.contains(keyword);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        if (super.equals(o)) {
            Magazine m = (Magazine) o;
            return this.publishingHouse.equals(m.publishingHouse);
        }
        return false;
    }

    //messo perchè equals da solo dava warning, e cercando su stack overflow ogni talvolta che overrido equal dovrei 
    //farlo anche con hashCode ma ora non ho voglia di capire come funziona di preciso, ho solo capito che viene invocato 
    //dalle hashmap e hashset per metterli come chiave di una tupla, se sono uguali metterli nello stesso pair se no metterlo in una posizione diversa;
    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
