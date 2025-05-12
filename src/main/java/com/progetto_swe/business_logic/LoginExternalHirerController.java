package com.progetto_swe.business_logic;

import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.domain_model.Token;
import com.progetto_swe.orm.HirerDAO;

import java.util.HashMap;

public class LoginExternalHirerController {
    public Hirer loginExternalHirer(String userCode, String password){
        HirerDAO hirerDAO = new HirerDAO();
        HashMap<String, String> saltAndHashedPassword = hirerDAO.getSaltAndHashedPassword(userCode);

        //controllo password
        if(!Hasher.hashPassword(password,saltAndHashedPassword.get("salt")).equals(saltAndHashedPassword.get("hashedPassword"))){
            return null;
        }

        //istanziazione Hirer
        Hirer hirer = hirerDAO.getHirer(userCode);
        hirer.setToken(new Token(hirer));
        return hirer;
    }
}
