package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.UserCredentials;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserCredentialsTest {

    @Test
    public void testConstructor(){
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");

        assertEquals("usercode", ucs.getUserCode());
        assertEquals("hashed_password", ucs.getHashedPassword());
    }

    @Test
    public void testEquals(){
        UserCredentials ucs1 = new UserCredentials("usercode_1", "hashed_password_1");
        UserCredentials ucs2 = new UserCredentials("usercode_2", "hashed_password_2");
        UserCredentials ucs3 = new UserCredentials("usercode_1", "hashed_password_1");

        assertEquals(ucs1, ucs3);
        assertNotEquals(ucs1, ucs2);
        assertNotEquals(null, ucs2);

    }
}
