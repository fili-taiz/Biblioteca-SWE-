package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectionManager {
    private static final ConnectionManager connectionManager = new ConnectionManager();
    private static Connection connection;

    private ConnectionManager() {
        try {
            String url = "jdbc:postgresql://localhost:5432/Biblioteca";
            String username = "postgres";
            String password = "HU12HUI26TAO";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Connection getConnection(){
        return connection;
    }

    public static void closeAutoCommit(){
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("Errore durante accesso al database");
        }
    }

    public static void openAutoCommit(){
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Errore durante accesso al database");
        }
    }

    public static void commit(){
        try {
            connection.commit();
            openAutoCommit();
        } catch (SQLException e) {
            System.out.println("Errore durante accesso al database");
        }
    }

    public static void rollback(){
        try {
            connection.rollback();
            openAutoCommit();
        } catch (SQLException e) {
            System.out.println("Errore durante accesso al database");
        }
    }

    public static void query(String query) {
        try {
            Statement statement = getConnection().createStatement();

            ResultSet resultSet = statement.executeQuery(query);

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
