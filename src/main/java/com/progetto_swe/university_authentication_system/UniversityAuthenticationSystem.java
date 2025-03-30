package com.progetto_swe.university_authentication_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UniversityAuthenticationSystem {

    private String url;
    private String username;
    private String password;
    private Connection connection;

    private final byte[] HEX_ARRAY = "0123456789abdcef".getBytes(StandardCharsets.US_ASCII);
    public String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }
    private boolean check(String password, String salt, String hashedPassword) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            String hashed = bytesToHex(md.digest((password+salt).getBytes(StandardCharsets.UTF_8)));
            return hashed.equals(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
        }
        return false;
    }

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

    public HashMap<String, String> getUniversityPeople(String userCode, String password) {
        HashMap<String, String> hirerInfo = new HashMap<>();
        try {
            getConnection();

            String query
                    = "SELECT U.salt, U.hashed_password "
                    + "FROM UniveristyPeople U"
                    + "WHERE U.usercode = '" + userCode + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                return null;
            }
            if(!check(password, resultSet.getString("salt"), resultSet.getString("hashed_password"))){
                return null;
            }
            query
                    = "SELECT * "
                    + "FROM UniveristyPeople U"
                    + "WHERE U.usercode = '" + userCode + "'; ";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                hirerInfo.put("userCode", userCode);
                hirerInfo.put("name", resultSet.getString("name"));
                hirerInfo.put("surname", resultSet.getString("surname"));
                hirerInfo.put("email", resultSet.getString("email"));
                hirerInfo.put("telephoneNumber", resultSet.getString("telephone_number"));
                hirerInfo.put("salt", resultSet.getString("salt"));
                hirerInfo.put("hashedPassword", resultSet.getString("hashed_password"));
                return hirerInfo;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }

    //creazione Admin con profilo
    public HashMap<String, String> getLibraryAdmin(String userCode, String password) {
        HashMap<String, String> adminInfo = new HashMap<>();
        try {
            getConnection();

            String query
                    = "SELECT L.salt, L.hashed_password "
                    + "FROM LibraryAdmin L"
                    + "WHERE L.usercode = '" + userCode + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                return null;
            }
            if(!check(password, resultSet.getString("salt"), resultSet.getString("hashed_password"))){
                return null;
            }
            query
                    = "SELECT * "
                    + "FROM LibraryAdmin L"
                    + "WHERE L.usercode = '" + userCode + "'; ";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                adminInfo.put("userCode", userCode);
                adminInfo.put("name", resultSet.getString("name"));
                adminInfo.put("surname", resultSet.getString("surname"));
                adminInfo.put("email", resultSet.getString("email"));
                adminInfo.put("telephoneNumber", resultSet.getString("telephone_number"));
                adminInfo.put("workingPlace", resultSet.getString("working_place"));
                adminInfo.put("salt", resultSet.getString("salt"));
                adminInfo.put("hashedPassword", resultSet.getString("hashed_password"));
                return adminInfo;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }
}
