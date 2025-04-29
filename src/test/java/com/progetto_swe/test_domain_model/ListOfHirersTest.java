package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.domain_model.ListOfHirers;
import com.progetto_swe.domain_model.UserCredentials;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ListOfHirersTest {

    @Test
    public void testConstructor(){
        ArrayList<Hirer> hirers = new ArrayList<>();
        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "11111", ucs_1, null);
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Hirer hirer_2 = new Hirer("usercode_2", "name_2", "surname_2", "email_2", "22222", ucs_2, LocalDate.of(2025, 5, 6));

        hirers.add(hirer_1);
        hirers.add(hirer_2);

        ListOfHirers listOfHirers = new ListOfHirers(hirers);

        assertEquals(hirers, listOfHirers.getHirers());
    }

    @Test
    public void testGetHirer(){
        ArrayList<Hirer> hirers = new ArrayList<>();
        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "11111", ucs_1, null);

        hirers.add(hirer_1);

        ListOfHirers listOfHirers = new ListOfHirers(hirers);

        assertEquals(hirer_1, listOfHirers.getHirer("usercode_1"));
        assertNull(listOfHirers.getHirer("usercode_2"));

    }

    @Test
    public void testSearchHirer(){
        ArrayList<Hirer> hirers = new ArrayList<>();
        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        Hirer hirer_1 = new Hirer("usercode_1", "nomeprimo", "surname_1", "email_1", "11111", ucs_1, null);
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Hirer hirer_2 = new Hirer("usercode_2", "nomesecondo", "surname_2", "email_2", "22222", ucs_2, null);
        UserCredentials ucs_3 = new UserCredentials("usercode_3", "hashed_password_3");
        Hirer hirer_3 = new Hirer("usercode_3", "nomesecondo", "surname_3", "email_3", "33333", ucs_3, null);


        hirers.add(hirer_1);
        hirers.add(hirer_2);
        hirers.add(hirer_3);

        ListOfHirers listOfHirers = new ListOfHirers(hirers);

        ArrayList<Hirer> h = new ArrayList<>();
        h.add(hirer_2);
        h.add(hirer_3);

        assertEquals(h, listOfHirers.searchHirer("nomesecondo"));
        assertNotEquals(hirers, listOfHirers.searchHirer("nomesecondo"));
    }
}
