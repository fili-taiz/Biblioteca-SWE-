package com.progetto_swe.business_logic;

import java.util.HashMap;

import com.progetto_swe.business_logic.business_logic_exception.AccessDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import com.progetto_swe.orm.database_exception.IdNotFoundException;
import com.progetto_swe.university_authentication_system.UniversityAuthenticationSystem;

public class AdminController {
    public Admin loginAdmin(String userCode, String password) throws AccessDeniedException {
        UniversityAuthenticationSystem authenticationSystem = new UniversityAuthenticationSystem();

        //ottengo informazioni di questo Admin se la password combacia con quella nel database universitario
        HashMap<String, String> adminInfo = authenticationSystem.getLibraryAdmin(userCode, password);

        //non riconosciuto dall'università
        if (adminInfo.isEmpty()) {
            throw new AccessDeniedException("Errore: accesso come ruolo Admin rifiutato, controlla userCode e password.");
        }

        //ottengo informazioni di questo Admin nel database bibliotecario
        AdminDAO adminDAO = new AdminDAO();
        Admin admin;
        try{
            admin = adminDAO.getAdmin(userCode);
        }catch(IdNotFoundException e){
            admin = null;
        }

        //riconosciuto dall'università ma è la prima volta che esegue login
        if (admin == null){
            adminDAO.addAdmin(userCode, adminInfo.get("name"), adminInfo.get("surname"), adminInfo.get("email"), adminInfo.get("telephoneNumber"), adminInfo.get("workingPlace"));

            admin = adminDAO.getAdmin(userCode);
        }

        //aggiunta credenziali
        admin.setToken(new Token(admin));

        //riconosciuto dal sistema universitario e presente nel database della biblioteca
        return admin;
    }
}