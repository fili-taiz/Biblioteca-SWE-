package com.progetto_swe.test.domain_model;

import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.domain_model.Library;
import com.progetto_swe.domain_model.UserCredentials;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AdminTest {

    @Test
    public void testConstructor() {
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        Admin admin = new Admin("usercode", "name", "surname", "email", "00000", Library.LIBRARY_1, ucs);

        assertEquals("usercode", admin.getUserCode());
        assertEquals("name", admin.getName());
        assertEquals("surname", admin.getSurname());
        assertEquals("email", admin.getEmail());
        assertEquals("00000", admin.getTelephoneNumber());
        assertEquals(Library.LIBRARY_1, admin.getWorkingPlace());
        assertEquals(ucs, admin.getUserProfile());
    }

}
