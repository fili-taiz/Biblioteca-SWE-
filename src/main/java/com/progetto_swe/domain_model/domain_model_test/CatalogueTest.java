package com.progetto_swe.domain_model.domain_model_test;

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
        Catalogue catalogue1 = new Catalogue(items);
        Catalogue catalogue2 = new Catalogue(null);

        assertEquals(items, catalogue1.getItems());
        assertNull(catalogue2.getItems());
    }

    @Test
    public void testSearchItem(){
        Item item_1 = new Item(1, "title1", LocalDate.of(2025, 4, 2), Language.LANGUAGE_1, Category.CATEGORY_1, "link1");
        Item item_2 = new Item(2, "title2", LocalDate.of(2025, 4, 3), Language.LANGUAGE_2, Category.CATEGORY_2, "link2");
        ArrayList<Item> items = new ArrayList<>();
        items.add(item_1);
        items.add(item_2);

        ArrayList<Item> result = new ArrayList<>();
        result.add(item_1);

        Catalogue catalogue = new Catalogue(items);

        assertEquals(result, catalogue.searchItem("itl", Category.CATEGORY_1));
        assertNotEquals(result, catalogue.searchItem("itl", Category.CATEGORY_2));

    }

    @Test
    public void testContains(){
        Item item_1 = new Item(1, "title1", LocalDate.of(2025, 4, 2), Language.LANGUAGE_1, Category.CATEGORY_1, "link1");
        Item item_2 = new Item(2, "title2", LocalDate.of(2025, 4, 3), Language.LANGUAGE_2, Category.CATEGORY_2, "link2");
        ArrayList<Item> items = new ArrayList<>();
        items.add(item_1);
        Catalogue catalogue = new Catalogue(items);
        assertEquals(item_1.getCode(), catalogue.contains(item_1));
        assertEquals(-1, catalogue.contains(item_2));
    }

    @Test
    public void testGetItem(){
        Item item_1 = new Item(1, "title1", LocalDate.of(2025, 4, 2), Language.LANGUAGE_1, Category.CATEGORY_1, "link1");
        Item item_2 = new Item(2, "title2", LocalDate.of(2025, 4, 3), Language.LANGUAGE_2, Category.CATEGORY_2, "link2");
        ArrayList<Item> items = new ArrayList<>();
        items.add(item_1);
        Catalogue catalogue = new Catalogue(items);
        assertEquals(item_1, catalogue.getItem(item_1.getCode()));
        assertNull(catalogue.getItem(item_2.getCode()));

    }
}
