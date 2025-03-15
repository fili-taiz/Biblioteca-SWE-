package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.progetto_swe.domain_model.Catalogue;
import com.progetto_swe.domain_model.Hirer;

/* probabile che non possa servire */
public class CatalogueDAO {
    private Connection connection;

    public CatalogueDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public Catalogue getCatalogue(){
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

        /**/

        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return new Catalogue();
    }

    public Hirer updatCatalogue(Catalogue Catalogue){
        this.connection = ConnectionManager.getConnection();
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
