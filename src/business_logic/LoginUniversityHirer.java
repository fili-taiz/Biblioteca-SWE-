package business_logic;

import domain_model.Hirer;
import orm.HirerDAO;

public class LoginUniversityHirer {
    public Hirer login(String userName, String password){
        HirerDAO hirerDAO = new HirerDAO();
        return hirerDAO.login(userName, password);
    }
}
