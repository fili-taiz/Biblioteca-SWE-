package orm;

import domain_model.*;
import java.sql.*;

public class MagazineDAO {
    private Connection connection;

    public MagazineDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public Magazine getMagazine(String itemId){
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
        return new Magazine();
    }

    public boolean updateMagazine(String parametriRimozioneMagazine){
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
        return false;
    }

    public boolean addMagazine(String parametriAggiuntaMagazine){
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
        return false;
    }

    public boolean removeMagazine(String parametriRimozioneMagazine){
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
        return false;
    }

}
