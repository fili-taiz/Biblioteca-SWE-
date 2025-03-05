package orm;

import domain_model.*;
import java.sql.*;

public class BookDAO {
    private Connection connection;

    public BookDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public Book getBook(String itemId){
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
        return new Book();
    }

    public boolean updateBook(String parametriAggiornamentoBook){
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

    public boolean addBook(String parametriAggiuntaBook){
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

    public boolean removeBook(String parametriRimozioneBook){
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
