package com.progetto_swe.orm;

import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.nio.file.*;
import java.io.IOException;
import java.util.List;

public class ConnectionManager {
    private static ConnectionManager connectionManager;
    private Connection connection;
    private String url = "";
    private String username = "";
    private String password = "";

    private ConnectionManager() {
        try {
            List<String> righe = Files.readAllLines(Paths.get("./src/main/resources/credenziali"));
            url = righe.get(2);
            username = righe.get(3);
            password = righe.get(4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public static ConnectionManager getInstance() {
        if (connectionManager == null) {
            connectionManager = new ConnectionManager();
        }
        return connectionManager;
    }

    public void setConnection(Connection newConnection) {
        connection = newConnection;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, username, password);
            }
            return connection;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public void closeAutoCommit() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public void openAutoCommit() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }
}
