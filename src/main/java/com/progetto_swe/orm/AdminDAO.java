package com.progetto_swe.orm;

import java.sql.*;

import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.domain_model.Library;
import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;

public class AdminDAO {
    private Connection connection;

    public AdminDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public Admin getAdmin(String userCode){
        connection = ConnectionManager.getConnection();
        try {
            String query  = "SELECT * FROM Admin A WHERE A.user_code = ?";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return new Admin(userCode, resultSet.getString("name"), resultSet.getString("surname"),
                        resultSet.getString("email"), resultSet.getString("telephone_number"),
                        Library.valueOf(resultSet.getString("working_place")), null);
            } else{
                System.out.println("There is no admin in the database with usercode " + userCode + "!");
                return null;
            }
        } catch (SQLException e) {
            throw new CRUD_exception("Error executing query!", e);
        }
    }

    public boolean addAdmin(String userCode, String name, String surname, String email, String telephoneNumber, String workingPlace) {
        connection = ConnectionManager.getConnection();
        try {
            String query = "INSERT INTO Admin (user_code, name, surname, email, telephone_number, working_place)"
                    + "VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setString(2, name);
            ps.setString(3, surname);
            ps.setString(4, email);
            ps.setString(5, telephoneNumber);
            ps.setString(6, workingPlace);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }
}
