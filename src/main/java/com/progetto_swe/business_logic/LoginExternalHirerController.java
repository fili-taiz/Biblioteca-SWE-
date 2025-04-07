package com.progetto_swe.business_logic;

import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.domain_model.UserCredentials;
import com.progetto_swe.orm.HirerDAO;

import java.util.HashMap;

public class LoginExternalHirerController {
    /*
    public Hirer login(String userCode, String password){
        HirerDAO hirerDAO = new HirerDAO();
        HashMap<String, String> saltAndHashedPassword = hirerDAO.getSaltAndHashedPassword(userCode);

        //controllo password
        if(!Hasher.hash(password,saltAndHashedPassword.get("salt")).equals(saltAndHashedPassword.get("hashedPassword"))){
            return null;
        }

        //istanziazione Hirer
        Hirer hirer = hirerDAO.getHirer(userCode);
        hirer.setUserCredentials(new UserCredentials(userCode,saltAndHashedPassword.get("hashedPassword")));
        return hirer;
    }
     */
}
