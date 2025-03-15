package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.progetto_swe.domain_model.Catalogue;
import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.domain_model.Item;
import com.progetto_swe.domain_model.UserProfile;

public class HirerDAO {

    private Connection connection;

    public HirerDAO() {

    }

    //creazione Hirer con profilo
    public Hirer login(String userCode, String password) {
        try {
            connection = ConnectionManager.getConnection();
            String query
                    = "SELECT * "
                    + "FROM UniveristyPeople U"
                    + "WHERE U.usercode = '" + userCode + "'";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return new Hirer(userCode, resultSet.getString("name"), resultSet.getString("surname"),
                        resultSet.getString("email"), resultSet.getString("telephone_number"), new UserProfile(userCode, password));
            }
            return null;
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return null;
    }

    public Catalogue refreshCatalogue() {
        ArrayList<Item> newCatalogue = new ArrayList<>();
        BookDAO bookDAO = new BookDAO();
        newCatalogue.addAll(bookDAO.getAllBook());
        MagazineDAO magazineDAO = new MagazineDAO();
        newCatalogue.addAll(magazineDAO.getAllMagazine());
        ThesisDAO thesisDAO = new ThesisDAO();
        newCatalogue.addAll(thesisDAO.getAllThesis());
        return new Catalogue(newCatalogue);
    }

    //creazione Hirer con solo i dati
    public Hirer getHirer(String userCode) {
        try {
            String query
                    = "SELECT * "
                    + "FROM UniveristyPeople U"
                    + "WHERE U.user_code = '" + userCode + "'";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return new Hirer(userCode, resultSet.getString("name"), resultSet.getString("surname"),
                        resultSet.getString("email"), resultSet.getString("telephone_number"), null);
            }
        } catch (SQLException e) {
        }
        return null;
    }

    /*
    
    public Hirer getHirer(String usercode, UserProfile userProfile) {
        
    }

     */
    public Hirer getHirer(String usercode, String password) {
        try {
            connection = ConnectionManager.getConnection();
            String query = "query ricerca un utente da codice";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

            /* ...  */
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }

    public boolean setHirer(String parametri) {
        try {
            connection = ConnectionManager.getConnection();
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

            /**/
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }

    public Hirer updatHirer(Hirer hirer) {
        try {
            connection = ConnectionManager.getConnection();
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery(query);

            resultSet.next();

            /**/
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }

    public boolean addHirer(String userCode, String password, String name, String surname, String email, String telephoneNumber) {
        connection = ConnectionManager.getConnection();
        try {
            String query = "INSERT INTO Hirer (user_code, password, name, surname, email, telephone_number)"
                    + "VALUES ('" + userCode + "', '" + password + "', '" + name + "', '" + surname + "', '" + email + "', '" + telephoneNumber + "');";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            return resultSet.next();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }

    public String addExternalHirer(String password, String name, String surname, String email, String telephoneNumber) {
        try {
            connection = ConnectionManager.getConnection();
            ResultSet resultSet;
            String userCode;

            //generazione codice univoco per chiave primaria con prefisso E per non occupare future possibili matricole
            do {
                userCode = "E" + Math.round((Math.random() * 1000000));
                String query
                        = "SELECT * "
                        + "FROM Hirer H"
                        + "WHERE H.user_code = '" + userCode + "'";
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
            } while (resultSet.next());

            String query = "INSERT INTO Hirer (user_code, password, name, surname, email, telephone_number)"
                    + "VALUES ('" + userCode + "'', '" + password + "', '" + name + "', '" + surname + "', '" + email + "', '" + telephoneNumber + "'');";
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            resultSet.next();

            return userCode;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return "";
    }

    public ArrayList<Hirer> getAllHirers() {
        ArrayList<Hirer> result = new ArrayList<>();
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT * "
                    + "FROM Item I JOIN Book B ON I.code = B.code";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Hirer h = new Hirer(resultSet.getString("user_code"), resultSet.getString("name"),
                        resultSet.getString("surname"), resultSet.getString("email"), resultSet.getString("telephone_number"), null);
                result.add(h);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return result;
    }

}
