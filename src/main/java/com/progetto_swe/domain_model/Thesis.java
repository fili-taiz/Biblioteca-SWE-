package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Thesis extends Item {
    private String author;
    private String supervisors;
    private String university;

    public Thesis(int code,
                  String title,
                  LocalDate publicationDate,
                  Language language,
                  Category category,
                  String link,
                  int number_of_pages,
                  String author,
                  String supervisors,
                  String university) {
        super(code, title, publicationDate, language, category, link, number_of_pages);
        this.author = author;
        this.supervisors = supervisors;
        this.university = university;
    }

    public Thesis(String title,
                  LocalDate publicationDate,
                  Language language,
                  Category category,
                  String link,
                  int number_of_pages,
                  String author,
                  String supervisors,
                  String university) {
        super(-1, title, publicationDate, language, category, link, number_of_pages);
        this.author = author;
        this.supervisors = supervisors;
        this.university = university;
    }

    @Override
    public String[] getValues() {
        return new String[]{Integer.toString(code), title, author, category.toString(), publicationDate.toString()};
    }


    @Override
    public ArrayList<String[]> toStringValues() {
        ArrayList<String[]> data = new ArrayList<>();
        data.add(new String[]{"Codice", Integer.toString(code)});
        data.add(new String[]{"Titolo", title});
        data.add(new String[]{"Autore", author});
        data.add(new String[]{"Data pubblicazione", publicationDate.toString()});
        data.add(new String[]{"Università", university});
        data.add(new String[]{"Supervisori", supervisors});
        data.add(new String[]{"Lingua", language.toString()});
        data.add(new String[]{"Categoria", category.toString()});
        data.add(new String[]{"Link", link});
        data.add(new String[]{"Num. pag.", Integer.toString(numberOfPages)});
        return data;
    }

    @Override
    public boolean sameField(Item itemsCopy) {
        if (!super.sameField(itemsCopy)) {
            return false;
        }

        if (itemsCopy instanceof Thesis thesisCopy) {
            if (!this.author.equals(thesisCopy.getAuthor())) {
                return false;
            }
            if (!this.supervisors.equals(thesisCopy.getSupervisors())) {
                return false;
            }
            return this.university.equals(thesisCopy.getUniversity());
        } else {
            return false;
        }
    }

    @Override
    public boolean contains(String keyword) {
        if (author.toUpperCase().contains(keyword.toUpperCase())) {
            return true;
        }
        if (supervisors.toUpperCase().contains(keyword.toUpperCase())) {
            return true;
        }
        if (university.toUpperCase().contains(keyword.toUpperCase())) {
            return true;
        }
        return super.contains(keyword);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        Thesis t = (Thesis) o;
        return this.author.equals(t.author) && this.supervisors.equals(t.supervisors) && this.university.equals(t.university);
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

}
