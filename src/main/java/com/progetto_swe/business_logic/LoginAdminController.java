package com.progetto_swe.business_logic;

import java.util.HashMap;

import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.orm.AdminDAO;
import com.progetto_swe.university_authentication_system.UniversityAuthenticationSystem;

public class LoginAdminController{

    public Admin login(String userCode, String password){
        UniversityAuthenticationSystem authenticationSystem = new UniversityAuthenticationSystem();
        HashMap<String, String> map = authenticationSystem.getUniversityPeople(userCode, password);

        //non riconosciuto dall'università
        if (map == null) { 
            return null;
        }


        AdminDAO adminDAO = new AdminDAO();
        Admin admin = adminDAO.getAdmin(userCode);

        //riconosciuto dall'università ma è la prima volta che esegue login
        if (admin == null){ 
            if (!adminDAO.addAdmin(userCode, password, map.get("name"), map.get("surname"), map.get("email"), map.get("telephoneNumber"), map.get("workingPlace"))) {
                //Aggiunta nel db fallito
                return null; 
            }
            return adminDAO.login(userCode, password);
        }

        //riconosciuto dal sistema universitario e presente nel db della biblioteca
        return admin;






    }
}