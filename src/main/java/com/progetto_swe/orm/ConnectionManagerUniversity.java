package com.progetto_swe.orm;

import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ConnectionManagerUniversity {
    private static ConnectionManagerUniversity connectionManagerUniversity;
    private Connection connection;
    private String url = "";
    private String username = "";
    private String password = "";

    private ConnectionManagerUniversity() {
        try {
            List<String> righe = Files.readAllLines(Paths.get("./src/main/resources/credenziali"));
            url = righe.get(6);
            username = righe.get(7);
            password = righe.get(8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public static ConnectionManagerUniversity getInstance() {
        if (connectionManagerUniversity == null) {
            connectionManagerUniversity = new ConnectionManagerUniversity();
        }
        return connectionManagerUniversity;
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
}
