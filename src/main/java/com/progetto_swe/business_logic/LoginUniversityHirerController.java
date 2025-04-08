package com.progetto_swe.business_logic;


import java.util.HashMap;

import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.domain_model.UserCredentials;
import com.progetto_swe.orm.HirerDAO;
import com.progetto_swe.university_authentication_system.UniversityAuthenticationSystem;

public class LoginUniversityHirerController {

    public Hirer loginUniversityHirer(String userCode, String password){
        UniversityAuthenticationSystem authenticationSystem = new UniversityAuthenticationSystem();

        //ottengo informazioni di questo UniversityHirer se la password combacia con quella nel database universitario
        HashMap<String, String> hirerInfo = authenticationSystem.getUniversityPeople(userCode, password);

        //non riconosciuto dall'università
        if (hirerInfo.isEmpty()) {
            return null;
        }
        //TODO implementare grafica controllo che non sia nullo

        //ottengo informazioni di questo UniversityHirer nel database bibliotecario
        HirerDAO hirerDAO = new HirerDAO();
        Hirer hirer = hirerDAO.getHirer(userCode);

        //riconosciuto dall'università ma è la prima volta che esegue login
        if (hirer == null){ 
            if (!hirerDAO.addHirer(userCode, hirerInfo.get("name"), hirerInfo.get("surname"), hirerInfo.get("email"), hirerInfo.get("telephoneNumber"))) {
                //Aggiunta nel db fallito
                return null; 
            }
            hirer = hirerDAO.getHirer(userCode);
        }

        //aggiunta credenziali
        hirer.setUserCredentials(new UserCredentials(userCode, hirerInfo.get("hashedPassword")));

        //riconosciuto dal sistema universitario e presente nel database della biblioteca
        return hirer;
    }
}
