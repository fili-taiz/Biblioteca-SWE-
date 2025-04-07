package com.progetto_swe.test.domain_model;

import com.progetto_swe.domain_model.Catalogue;
import com.progetto_swe.domain_model.Category;
import com.progetto_swe.domain_model.Item;
import com.progetto_swe.domain_model.Language;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CatalogueTest {

    @Test
    public void testConstructor(){
        ArrayList<Item> items = new ArrayList<>();
        Catalogue catalogue_1 = new Catalogue(items);
        Catalogue catalogue_2 = new Catalogue(null);

        assertEquals(items, catalogue_1.getItems());
        assertEquals(items, catalogue_2.getItems());
    }

    @Test
    public void testContains(){
        Item item_1 = new Item(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100);
        Item item_2 = new Item(2, "title_2", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", 150);
        ArrayList<Item> items = new ArrayList<>();
        items.add(item_1);
        Catalogue catalogue = new Catalogue(items);

        assertEquals(1, catalogue.contains(item_1));
        assertEquals(-1, catalogue.contains(item_2));
    }

    @Test
    public void testGetCode(){
        Item item_1 = new Item(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100);
        Item item_2 = new Item(2, "title_2", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", 150);
        ArrayList<Item> items = new ArrayList<>();
        items.add(item_1);
        Catalogue catalogue = new Catalogue(items);

        assertEquals(item_1, catalogue.getItem(1));
        assertNull(catalogue.getItem(2));
    }
}
