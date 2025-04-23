package com.progetto_swe.orm;

import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

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
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public static Connection getConnection(){
        return connection;
    }

    public static void closeAutoCommit(){
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public static void openAutoCommit(){
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public static void commit(){
        try {
            connection.commit();
            openAutoCommit();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public static void rollback(){
        try {
            connection.rollback();
            openAutoCommit();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}
