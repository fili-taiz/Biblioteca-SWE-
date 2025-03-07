package orm;

import domain_model.*;
import java.sql.*;

public class HirerDAO {
    private Connection connection;

    public HirerDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public Hirer login(String username, String password){
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

        /* */
            String usercode = "0000";
        /* */
            if(true/* */){
                return getHirer(usercode);
            }

        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }

    public Hirer getHirer(String usercode){
        try {
            String query = "query ricerca un utente da codice";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

        /* ...  */
        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return new Hirer();
    }

    public boolean setHirer(String parametri){
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

        /**/

        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }

    public Hirer updatHirer(Hirer hirer){
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery(query);

            resultSet.next();

        /**/

        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return new Hirer();
    }

    public boolean addHirer(String name, String surname, String IDcode, String eMail, String telephoneNumber){
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

        /**/

        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }
}
