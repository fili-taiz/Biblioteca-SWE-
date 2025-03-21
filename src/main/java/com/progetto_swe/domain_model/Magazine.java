package com.progetto_swe.domain_model;

import java.time.LocalDate;

public class Magazine extends Item {

    String publishingHouse;

    public Magazine(int code, String title, LocalDate publicationDate, Language language, Category category, String link, boolean borrowable,
                    String publishingHouse) {
        super(code, title, publicationDate, language, category, link, borrowable);
        this.publishingHouse = publishingHouse;
    }

    public Magazine(String title, LocalDate publicationDate, Language language, Category category, String link, boolean borrowable,
                    String publishingHouse) {
        super(-1, title, publicationDate, language, category, link, borrowable);
        this.publishingHouse = publishingHouse;
    }


    @Override
    public boolean sameField(Item itemsCopy) {
        if (!super.sameField(itemsCopy)) {
            return false;
        }

        Magazine magazinesCopy = (Magazine) itemsCopy;
        if (!this.publishingHouse.equals(magazinesCopy.getPublishingHouse())) {
            return false;
        }
        return true;
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
    public boolean contains(String keyword) {
        if (publishingHouse.toUpperCase().contains(keyword.toUpperCase())) {
            return true;
        }
        return super.contains(keyword);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        Magazine m = (Magazine) o;
        return this.publishingHouse.equals(m.publishingHouse);
    }

    //messo perchè equals da solo dava warning, e cercando su stack overflow ogni talvolta che overrido equal dovrei 
    //farlo anche con hashCode ma ora non ho voglia di capire come funziona di preciso, ho solo capito che viene invocato 
    //dalle hashmap e hashset per metterli come chiave di una tupla, se sono uguali metterli nello stesso pair se no metterlo in una posizione diversa;
    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
