package com.progetto_swe.business_logic;


import java.util.HashMap;

import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.domain_model.UserCredentials;
import com.progetto_swe.orm.AdminDAO;
import com.progetto_swe.university_authentication_system.UniversityAuthenticationSystem;

public class LoginAdminController{

    public Admin loginAdmin(String userCode, String password){
        UniversityAuthenticationSystem authenticationSystem = new UniversityAuthenticationSystem();

        //ottengo informazioni di questo Admin se la password combacia con quella nel database universitario
        HashMap<String, String> adminInfo = authenticationSystem.getUniversityPeople(userCode, password);

        //non riconosciuto dall'università
        if (adminInfo == null) {
            return null;
        }
        //TODO implementare grafica controllo che non sia nullo

        //ottengo informazioni di questo Admin nel database bibliotecario
        AdminDAO adminDAO = new AdminDAO();
        Admin admin = adminDAO.getAdmin(userCode);

        //riconosciuto dall'università ma è la prima volta che esegue login
        if (admin == null){
            if (!adminDAO.addAdmin(userCode, adminInfo.get("name"), adminInfo.get("surname"), adminInfo.get("email"), adminInfo.get("telephoneNumber"), adminInfo.get("workingPlace"))) {
                //Aggiunta nel db fallito
                return null; 
            }
            admin = adminDAO.getAdmin(userCode);
        }

        //aggiunta credenziali
        admin.setUserCredentials(new UserCredentials(userCode, adminInfo.get("hashedPassword")));

        //riconosciuto dal sistema universitario e presente nel database della biblioteca
        return admin;
    }
}