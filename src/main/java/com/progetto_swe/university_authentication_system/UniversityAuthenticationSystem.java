package com.progetto_swe.university_authentication_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class UniversityAuthenticationSystem {

    private String url;
    private String username;
    private String password;
    private Connection connection;

    private Connection getConnection() {
        try {
            url = "jdbc:postgresql://localhost:5432/Biblioteca";
            username = "postgres";
            password = "HU12HUI26TAO";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return connection;
    }

    public HashMap<String, String> getUniversityPeople(String usercode, String password) {
        try {
            getConnection();
            String query
                    = "SELECT * "
                    + "FROM UniveristyPeople U"
                    + "WHERE U.usercode = '" + usercode + "' AND L.password = '" + password + "'; ";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("userCode", usercode);
                map.put("name", resultSet.getString("name"));
                map.put("surname", resultSet.getString("surname"));
                map.put("email", resultSet.getString("email"));
                map.put("telephoneNumber", resultSet.getString("telephone_number"));
                return map;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }

    //creazione Admin con profilo
    public HashMap<String, String> getLibraryAdmin(String usercode, String passoword) {
        try {
            getConnection();
            String query
                    = "SELECT * "
                    + "FROM LibraryAdmin L"
                    + "WHERE L.usercode = '" + usercode + "' AND L.password = '" + passoword + "'; ";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("userCode", usercode);
                map.put("name", resultSet.getString("name"));
                map.put("surname", resultSet.getString("surname"));
                map.put("email", resultSet.getString("email"));
                map.put("telephoneNumber", resultSet.getString("telephone_number"));
                map.put("workingPlace", resultSet.getString("working_place"));
                return map;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }
}
