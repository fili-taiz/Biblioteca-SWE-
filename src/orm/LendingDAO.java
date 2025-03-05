package orm;

import domain_model.*;
import java.sql.*;

public class LendingDAO {
    private Connection connection;

    public LendingDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public Lending getLending(String itemCode, Biblioteca biblioteca, String hirerCode){
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
        return new Lending();
    }

    public boolean updatLending(Lending lending){
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

    public boolean addLending(String parametriAggiuntaLending){
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

    public boolean removeLending(String codice){
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
