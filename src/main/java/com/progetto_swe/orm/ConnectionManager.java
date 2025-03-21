package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectionManager {
    private static final ConnectionManager connectionManager = new ConnectionManager();
    private static Connection connection;
    private static String url = "jdbc:postgresql://localhost:5432/Biblioteca";;
    private static String username = "postgres";
    private static String password = "HU12HUI26TAO";

    private ConnectionManager() {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
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
        } catch (SQLException e) {
            System.out.println("Errore durante accesso al database");
        }
    }

    public static void rollback(){
        try {
            connection.rollback();
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
