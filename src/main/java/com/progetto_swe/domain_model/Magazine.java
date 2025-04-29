package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Magazine extends Item {
    private String publishingHouse;

    public Magazine(int code, String title, LocalDate publicationDate, Language language, Category category, String link, int number_of_pages,
                    String publishingHouse) {
        super(code, title, publicationDate, language, category, link, number_of_pages);
        this.publishingHouse = publishingHouse;
    }

    public Magazine(String title, LocalDate publicationDate, Language language, Category category, String link, int number_of_pages,
                    String publishingHouse) {
        super(-1, title, publicationDate, language, category, link, number_of_pages);
        this.publishingHouse = publishingHouse;
    }


    @Override
    public ArrayList<String[]> toStringValues() {
        ArrayList<String[]> data = new ArrayList<>();
        data.add(new String[]{"Codice: ", Integer.toString(this.code)});
        data.add(new String[]{"Titolo: ", this.title});
        data.add(new String[]{"Casa editrice: ", this.publishingHouse});
        data.add(new String[]{"Data pubblicazione: ", this.publicationDate.toString()});
        data.add(new String[]{"Categoria: ", this.category.toString()});
        data.add(new String[]{"Link: ", this.link});
        return data;
    }

    @Override
    public String[] getValues(){
        return new String[]{this.title, this.publishingHouse, this.category.name(), this.publicationDate.toString()};
    }

    @Override
    public boolean sameField(Item itemsCopy) {
        if (!super.sameField(itemsCopy)) {
            return false;
        }
        if(itemsCopy instanceof Magazine magazineCopy) {
            return this.publishingHouse.equals(magazineCopy.getPublishingHouse());
        } else{
            return false;
        }
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

    public String getPublishingHouse() { return this.publishingHouse; }
}
