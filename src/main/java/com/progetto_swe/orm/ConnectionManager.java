package com.progetto_swe.orm;

import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static Connection connection;
    private static final String url = "jdbc:postgresql://localhost:5432/Library";
    private static final String username = "postgres";
    private static final String password = "filipposwe";

    private ConnectionManager() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public static void setConnection(Connection newConnection) {
        connection = newConnection;
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, username, password);
            }
            return connection;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public static void closeAutoCommit() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public static void openAutoCommit() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public static void commit() {
        try {
            connection.commit();
            openAutoCommit();
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public static void rollback() {
        try {
            connection.rollback();
            openAutoCommit();
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }
}
