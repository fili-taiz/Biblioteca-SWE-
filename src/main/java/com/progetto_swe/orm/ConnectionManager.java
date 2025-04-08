package com.progetto_swe.orm;

import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {

    private static Connection connection = null;
    private static boolean isTestConnection = false;

    public static void setTestConnection(Connection newConnection) {
        connection = newConnection;
        isTestConnection = true;
    }

    public static void clearTestConnection() {
        connection = null;
        isTestConnection = false;
    }

    public static Connection getConnection() {
        if (connection == null) {
            if (isTestConnection) {
                throw new DatabaseConnectionException("Test connection not set!", null);
            }
            try {
                String url = "jdbc:postgresql://localhost:5432/Biblioteca";
                String username = "postgres";
                String password = "HU12HUI26TAO";
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new DatabaseConnectionException("Connection error!", e);
            }
        }
        return connection;
    }

    public static void closeAutoCommit() {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public static void openAutoCommit() {
        try {
            getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public static void commit() {
        try {
            getConnection().commit();
            openAutoCommit();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public static void rollback() {
        try {
            getConnection().rollback();
            openAutoCommit();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public static void query(String query) {
        try {
            Statement statement = getConnection().createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            if (!resultSet.next()) {
                throw new DataAccessException("Error executing query!", null);
            }

            while (resultSet.next()) {
                System.out.println("Id: " + resultSet.getString("userCode")
                        + ", Name: " + resultSet.getString("name")
                        + ", Surname: " + resultSet.getString("surname") + ";");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}
