package com.progetto_swe.orm;

import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.nio.file.*;
import java.io.IOException;
import java.util.List;

public class ConnectionManager {//TODO implementare in modo più appropriato il singleton
    private static ConnectionManager connectionManager;
    private static Connection connection;
    private static String url = "";
    private static final String username = "postgres";
    private static String password = "";

    private ConnectionManager() {
        try {
            List<String> righe = Files.readAllLines(Paths.get("./src/main/resources/credenziali"));
            url = righe.get(2);
            password = righe.get(3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public static void setConnection(Connection newConnection) {
        connection = newConnection;
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connectionManager = new ConnectionManager();
                connection = DriverManager.getConnection(url, username, password);
            }
            return connection;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public static void closeAutoCommit() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public static void openAutoCommit() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public static void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public static void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }
}
