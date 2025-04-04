package com.progetto_swe.domain_model.domain_model_test;

import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.domain_model.ListOfHirers;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ListOfHirersTest {

    @Test
    public void testConstructor(){
        ArrayList<Hirer> hirers = new ArrayList<>();
        ListOfHirers listOfHirers = new ListOfHirers(hirers);

        assertEquals(hirers, listOfHirers.getHirers());
    }

    @Test
    public void testGetHirer(){
        Hirer h1 = new Hirer("usercode1", "name1", "surname1", "email1", "00001", null, null, null, null, null);
        Hirer h2 = new Hirer("usercode2", "name2", "surname2", "email2", "00002", null, null, null, null, null);

        ArrayList<Hirer> hirers = new ArrayList<>();

        hirers.add(h1);
        hirers.add(h2);

        ListOfHirers listOfHirers = new ListOfHirers(hirers);

        assertEquals(h1, listOfHirers.getHirer("usercode1"));
        assertNull(listOfHirers.getHirer("usercode4"));

    }

    @Test
    public void testSearchHirer(){
        Hirer h1 = new Hirer("usercode1", "name1", "surname1", "email1", "00001", null, null, null, null, null);
        Hirer h2 = new Hirer("usercode2", "name2", "surname2", "email2", "00002", null, null, null, null, null);
        Hirer h3 = new Hirer("usercode3", "name3", "surname3", "email3", "00013", null, null, null, null, null);

        ArrayList<Hirer> hirers = new ArrayList<>();
        hirers.add(h1);
        hirers.add(h2);

        ListOfHirers listOfHirers = new ListOfHirers(hirers);

        assertEquals(listOfHirers.getHirers(), listOfHirers.searchHirer("0000"));

        listOfHirers.getHirers().add(h3);

        assertNotEquals(listOfHirers.getHirers(), listOfHirers.searchHirer("0000"));
    }
}
