package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.domain_model.Catalogue;
import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.domain_model.Item;
import com.progetto_swe.domain_model.Library;
import com.progetto_swe.domain_model.ListOfHirers;
import com.progetto_swe.domain_model.UserCredentials;
import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

public class AdminDAO {
    private Connection connection;

    public AdminDAO(Connection connection){
        this.connection = connection;
    }

    public Admin getAdmin(String userCode){
        try {
            String query
                    = "SELECT * "
                    + "FROM Admin A "
                    + "WHERE A.user_code = '" + userCode + "'";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return new Admin(userCode, resultSet.getString("name"), resultSet.getString("surname"),
                        resultSet.getString("email"), resultSet.getString("telephone_number"),
                        Library.valueOf(resultSet.getString("working_place")), null);
            } else{
                throw new DataAccessException("Error executing query!", null);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean addAdmin(String userCode, String name, String surname, String email, String telephoneNumber, String workingPlace) {
        connection = ConnectionManager.getConnection();
        try {
            String query = "INSERT INTO Admin (user_code, name, surname, email, telephone_number, working_place)"
                    + "VALUES ('" + userCode + "', '" + name + "', '" + surname + "', '" + email + "', '" + telephoneNumber + "', '"
                    + workingPlace + "');";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                return true;
            }else{
                throw new CRUD_exception("Error executing insert!", null);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}
