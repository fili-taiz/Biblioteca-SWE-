package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {

    @Test
    public void testConstructor() {
        Admin admin = new Admin("usercode", "name", "surname", "email", "00000", Library.LIBRARY_1, null);
        Token token = new Token(admin);
        admin.setToken(token);

        assertEquals("usercode", admin.getUserCode());
        assertEquals("name", admin.getName());
        assertEquals("surname", admin.getSurname());
        assertEquals("email", admin.getEmail());
        assertEquals("00000", admin.getTelephoneNumber());
        assertEquals(Library.LIBRARY_1, admin.getWorkingPlace());
        assertEquals(token, admin.getToken());
    }

    @Test
    public void testEquals(){
        Admin admin1 = new Admin("usercode_1", "name", "surname", "email", "00000", Library.LIBRARY_1, null);
        Token token = new Token(admin1);
        admin1.setToken(token);
        Admin admin2 = new Admin("usercode_2", "name2", "surname2", "email2", "00002", Library.LIBRARY_1, null);
        Token token2 = new Token(admin2);
        admin2.setToken(token);
        Admin admin3 = new Admin("usercode_1", "name", "surname", "email", "00000", Library.LIBRARY_1, null);
        admin3.setToken(token);

        assertEquals(admin1, admin3);
        assertNotEquals(admin1, admin2);
        assertNotEquals(admin1, null);
    }

}

