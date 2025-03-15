package com.progetto_swe.domain_model;

import java.time.LocalDate;

public class Thesis extends Item {

    String author;
    String supervisors;
    String university;

    public Thesis(int code, String title, LocalDate publicationDate, boolean borrowable, Language language, Category category, String link, String author, String supervisors, String university) {
        super(code, title, publicationDate, language, category, link, borrowable);
        this.author = author;
        this.supervisors = supervisors;
        this.university = university;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getSupervisors() {
        return this.supervisors;
    }

    public String getUniversity() {
        return this.university;
    }

    public void setAuthor(String newAuthor) {
        this.author = newAuthor;
    }

    public void setSupervisors(String newSupervisors) {
        this.supervisors = newSupervisors;
    }

    public void setUniversity(String newUniversity) {
        this.university = newUniversity;
    }

    @Override
    public boolean updateItem(Item new_item) {
        if (new_item instanceof Thesis thesis) {
            super.updateItem(new_item);
            this.setAuthor(thesis.getAuthor());
            this.setSupervisors(thesis.getSupervisors());
            this.setUniversity(thesis.getUniversity());
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(String keyword){
        if(author.toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        if(supervisors.toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        if(university.toUpperCase().contains(keyword.toUpperCase())){
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
            Thesis t = (Thesis) o;
            return this.author.equals(t.author) && this.supervisors.equals(t.supervisors) && this.university.equals(t.university);
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
