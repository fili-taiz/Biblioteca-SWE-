package business_logic;

import domain_model.Admin;
import orm.AdminDAO;

public class LoginAdminController{
    public Admin login(String userName, String password){
        AdminDAO AdminDAO = new AdminDAO();
        return AdminDAO.getAdmin(userName, password);
    }
}