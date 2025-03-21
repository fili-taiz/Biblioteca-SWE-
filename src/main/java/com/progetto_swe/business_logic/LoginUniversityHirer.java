package com.progetto_swe.business_logic;

import java.util.HashMap;

import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.orm.HirerDAO;
import com.progetto_swe.university_authentication_system.UniversityAuthenticationSystem;

public class LoginUniversityHirer {

    public Hirer loginUniversityHirer(String userCode, String password){
        UniversityAuthenticationSystem authenticationSystem = new UniversityAuthenticationSystem();
        HashMap<String, String> map = authenticationSystem.getUniversityPeople(userCode, password);

        //non riconosciuto dall'università
        if (map == null) { 
            return null;
        }
        //TODO implementare grafica controllo che non sia nullo

        HirerDAO hirerDAO = new HirerDAO();
        Hirer hirer = hirerDAO.getHirer(userCode);

        //riconosciuto dall'università ma è la prima volta che esegue login
        if (hirer == null){ 
            if (!hirerDAO.addHirer(userCode, password, map.get("name"), map.get("surname"), map.get("email"), map.get("telephoneNumber"))) {
                //Aggiunta nel db fallito
                return null; 
            }
            return hirerDAO.login(userCode, password);
        }

        //riconosciuto dal sistema universitario e presente nel db della biblioteca
        return hirer;
    }
}
