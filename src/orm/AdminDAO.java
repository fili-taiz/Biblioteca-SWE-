package orm;

import domain_model.*;
import java.sql.*;

public class AdminDAO {
    private Connection connection;

    public AdminDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public Admin getAdmin(String username, String password){
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

        /**/

        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return new Admin();
    }

    public Hirer updatAdmin(Admin admin){
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

        /**/

        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return new Hirer();
    }
}
