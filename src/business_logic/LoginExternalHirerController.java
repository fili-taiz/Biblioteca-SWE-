package business_logic;

import domain_model.Hirer;
import orm.HirerDAO;

public class LoginExternalHirerController {
    public Hirer login(String username, String password){
        HirerDAO hirerDAO = new HirerDAO();
        return hirerDAO.login(username, password);
    }
}
