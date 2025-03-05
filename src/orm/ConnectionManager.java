package orm;

import java.sql.*;


public class ConnectionManager {

    private static String url = "jdbc:postgresql://localhost:5432/Biblioteca";;
    private static String username = "postgres";
    private static String password = "HU12HUI26TAO";


    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

    public void query(String query) {
        try {
            Statement statement = getConnection().createStatement();

            ResultSet resultSet;
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println("Id: " + resultSet.getString("userCode")
                        + ", Name: " + resultSet.getString("name")
                        + ", Surname: " + resultSet.getString("surname")
                        + ";");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
