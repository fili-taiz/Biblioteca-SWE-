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
import com.progetto_swe.domain_model.UserProfile;

public class AdminDAO {
    private Connection connection;

    public AdminDAO(){
        this.connection = ConnectionManager.getConnection();
    }

        //creazione Hirer con profilo
    public Admin login(String userCode, String password) {
        try {
            connection = ConnectionManager.getConnection();
            String query
                    = "SELECT * "
                    + "FROM UniveristyPeople U"
                    + "WHERE U.usercode = '" + userCode + "'";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return new Admin(userCode, resultSet.getString("name"), resultSet.getString("surname")
                        , resultSet.getString("email"), resultSet.getString("telephone_number")
                        , Library.valueOf(resultSet.getString("working_place")), new UserProfile(userCode, password));
            }
            return null;
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return null;
    }

    public Admin getAdmin(String userCode){
        try {
            String query
                    = "SELECT * "
                    + "FROM Admin A"
                    + "WHERE A.user_code = '" + userCode + "'";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return new Admin(userCode, resultSet.getString("name"), resultSet.getString("surname"), 
                        resultSet.getString("email"), resultSet.getString("telephone_number"), 
                        Library.valueOf(resultSet.getString("working_place")), null);
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public boolean addAdmin(String userCode, String password, String name, String surname, String email, String telephoneNumber, String workingPlace) {
        connection = ConnectionManager.getConnection();
        try {
            String query = "INSERT INTO Admin (user_code, hashed_password, name, surname, email, telephone_number, working_place)"
                    + "VALUES ('" + userCode + "', '" + password + "', '" + name + "', '" + surname + "', '" + email + "', '" + telephoneNumber + "', '"
                    + workingPlace + "');";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            return resultSet.next();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }

    public Catalogue refreshCatalogue(){
        ArrayList<Item> newCatalogue = new ArrayList<>(); 
        BookDAO bookDAO = new BookDAO();
        newCatalogue.addAll(bookDAO.getAllBook());
        MagazineDAO magazineDAO = new MagazineDAO();
        newCatalogue.addAll(magazineDAO.getAllMagazine());
        ThesisDAO thesisDAO = new ThesisDAO();
        newCatalogue.addAll(thesisDAO.getAllThesis());
        return new Catalogue(newCatalogue);
    }

    public ListOfHirers refreshHirers(){
        HirerDAO hirerDAO = new HirerDAO();
        return new ListOfHirers(hirerDAO.getAllHirers());
    }

    public Hirer updatAdmin(Admin admin){
        connection = ConnectionManager.getConnection();
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

        /**/

        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }
}
