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
}
