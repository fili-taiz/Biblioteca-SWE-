package com.progetto_swe.domain_model.domain_model_test;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class AdminTest {

    private UserCredentials ucs = new UserCredentials("code", "hashed_password");

    @Test
    public void testConstructor() {
        Admin admin = new Admin("code", "name", "surname", "email", "00000", Library.LIBRARY_1, ucs);
        assertEquals("code", admin.getUserCode());
        assertEquals("name", admin.getName());
        assertEquals("surname", admin.getSurname());
        assertEquals("email", admin.getEmail());
        assertEquals("00000", admin.getTelephoneNumber());
        assertEquals(Library.LIBRARY_1, admin.getWorkingPlace());
        assertEquals(ucs, admin.getUserProfile());
    }

    @Test
    public void testSearchItem(){
        Catalogue catalogue = new Catalogue(null);
        ArrayList<Item> items = new ArrayList<>();
        assertEquals(items, catalogue.searchItem("keywords", Category.CATEGORY_1));
    }

}

