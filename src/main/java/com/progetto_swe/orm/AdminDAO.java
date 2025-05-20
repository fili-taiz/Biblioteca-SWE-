package com.progetto_swe.orm;

import java.sql.*;

import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.domain_model.Library;
import com.progetto_swe.domain_model.Token;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;
import com.progetto_swe.orm.database_exception.IdAlreadyExistsException;
import com.progetto_swe.orm.database_exception.IdNotFoundException;

public class AdminDAO {
    private Connection connection;

    public AdminDAO(){
        this.connection = ConnectionManager.getInstance().getInstance().getConnection();
    }

    public Admin getAdmin(String userCode) throws IdNotFoundException, DatabaseConnectionException {
        connection = ConnectionManager.getInstance().getConnection();
        try {
            String query = """
                        SELECT * 
                        FROM Admin A 
                        WHERE A.user_code = ?
                        """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.next()) {
                throw new IdNotFoundException("Errore: Admin con userCode [" + userCode + "] non è presente nel DB.");
            }

            Admin admin = new Admin(
                    userCode,
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getString("email"),
                    resultSet.getString("telephone_number"),
                    Library.valueOf(resultSet.getString("working_place")),
                    null);
            admin.setToken(new Token(admin));
            return admin;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public void addAdmin(String userCode,
                         String name,
                         String surname,
                         String email,
                         String telephoneNumber,
                         String workingPlace)
            throws IdAlreadyExistsException, DatabaseConnectionException {
        connection = ConnectionManager.getInstance().getConnection();
        try {
            String query = """
                    INSERT INTO Admin (user_code, name, surname, email, telephone_number, working_place) 
                    VALUES (?, ?, ?, ?, ?, ?);
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setString(2, name);
            ps.setString(3, surname);
            ps.setString(4, email);
            ps.setString(5, telephoneNumber);
            ps.setString(6, workingPlace);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(e.getSQLState().equals("23505")){
                throw new IdAlreadyExistsException("Errore: Admin con userCode [" + userCode + "] già presente nel DB.");
            }
            throw new DatabaseConnectionException(e);
        }
    }
}
