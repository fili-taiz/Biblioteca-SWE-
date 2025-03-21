package com.progetto_swe;

import java.time.LocalDate;

import com.progetto_swe.domain_model.Category;
import com.progetto_swe.domain_model.Item;
import com.progetto_swe.domain_model.Language;
import com.progetto_swe.domain_model.Magazine;
import com.progetto_swe.orm.ConnectionManager;


public class Main {
    public static void main(String[] args) {
        clearScreen();
        ConnectionManager.query("Select * From Hirer");
        System.out.println(Math.round((Math.random()*1000000)));
/*ArrayList<String> a = new ArrayList<>();
a.add("C");
a.add("c");
a.add("a");
a.add("A");
        */Item magazine = new Magazine(1, "title", LocalDate.of(2012, 2, 20), Language.LANGUAGE_1, Category.CATEGORY_1, "link", true, "publishing_house_magazine");

        Item newMagazine = new Magazine(1, "title_1", LocalDate.of(2012, 2, 19), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", false, "publishing_house_magazine_1");

        /*
        Item i1 = new Item(1, "null", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "null", false);
        Item i2 = new Item(1, "null", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "null", false);
        */
        System.out.println(magazine.equals(newMagazine));
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  
}