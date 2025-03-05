package orm;

import domain_model.*;
import java.sql.*;

public class ThesisDAO {
    private Connection connection;

    public ThesisDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public Thesis getThesis(String itemId){
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
        return new Thesis();
    }

    public boolean updateThesis(String parametriAggiornamentoThesis){
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

    public boolean addThesis(String parametriAggiuntaThesis){
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

    public boolean removeThesis(String parametriRimozioneThesis){
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
