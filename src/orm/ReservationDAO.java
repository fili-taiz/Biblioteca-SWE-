package orm;

import domain_model.*;
import java.sql.*;

public class ReservationDAO {
    private Connection connection;

    public ReservationDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public Reservation getReservation(String itemCode, Library LIBRARY, String hirerCode){
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

        /**/

        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return new Reservation();
    }

    public Reservation updatReservation(Reservation reservation){
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

        /**/

        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return new Reservation();
    }

    public boolean addReservation(String parametriAggiuntaReservation){
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

    public boolean removeReservation(String parametriAggiuntaReservation){
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
