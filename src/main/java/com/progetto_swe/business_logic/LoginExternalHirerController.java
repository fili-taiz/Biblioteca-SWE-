package com.progetto_swe.business_logic;

import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.orm.HirerDAO;

public class LoginExternalHirerController {
    public Hirer login(String username, String password){
        HirerDAO hirerDAO = new HirerDAO();
        return hirerDAO.login(username, password);
    }
}
