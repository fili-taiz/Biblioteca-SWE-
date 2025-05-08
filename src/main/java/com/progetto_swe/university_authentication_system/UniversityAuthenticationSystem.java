package com.progetto_swe.university_authentication_system;

import java.sql.*;
import java.util.HashMap;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UniversityAuthenticationSystem {

    private String url;
    private String username;
    private String password;
    private Connection connection;
    private final byte[] HEX_ARRAY = "0123456789abcdef".getBytes(StandardCharsets.US_ASCII);

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
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            String hashed = bytesToHex(md.digest((password+salt).getBytes(StandardCharsets.UTF_8)));
            return hashed.equals(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private Connection getConnection() {
        try {
            url = "jdbc:postgresql://localhost:5432/University";
            username = "postgres";
            password = "filipposwe";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return connection;
    }

    public HashMap<String, String> getUniversityPeople(String userCode, String password) {
        HashMap<String, String> hirerInfo = new HashMap<>();
        try {
            getConnection();

            String query
                    = "SELECT U.salt, U.hashed_password "
                    + "FROM University_People U"
                    + " WHERE U.user_code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            if(!check(password, resultSet.getString("salt"), resultSet.getString("hashed_password"))){
                return null;
            }
            String query_2
                    = "SELECT * "
                    + "FROM University_People U"
                    + " WHERE U.user_code = ?; ";
            PreparedStatement ps2 = connection.prepareStatement(query_2);
            ps2.setString(1, userCode);
            resultSet = ps.executeQuery(query);
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
            System.out.println("SQLException: " + e.getMessage());
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
                    + "FROM Library_Admin L"
                    + " WHERE L.user_code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            if(!check(password, resultSet.getString("salt"), resultSet.getString("hashed_password"))){
                return null;
            }
            String query_2
                    = "SELECT * "
                    + "FROM Library_Admin L"
                    + " WHERE L.user_code = ?; ";
            PreparedStatement ps2 = connection.prepareStatement(query_2);
            ps2.setString(1, userCode);
            resultSet = ps2.executeQuery();
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
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }
}
